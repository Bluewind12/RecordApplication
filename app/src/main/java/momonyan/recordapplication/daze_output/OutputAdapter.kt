package momonyan.recordapplication.daze_output

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.input_daze_layout.view.*
import kotlinx.android.synthetic.main.picker_diarog_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.daze_database.AppDataBase

class OutputAdapter(private val mValues: ArrayList<OutputDataClass>) : RecyclerView.Adapter<OutputHolder>() {
    private lateinit var dataBase: AppDataBase
    private lateinit var activity: Activity

    private var color: Int = 0
    private var darkLight: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutputHolder {
        //レイアウトの設定(inflate)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.output_card_layout, parent, false)
        //Holderの生成
        return OutputHolder(view)
    }

    override fun onBindViewHolder(holder: OutputHolder, position: Int) {
        val item = mValues[position]
        holder.mOutputDateText.text = item.date
        holder.mOutputTitleText.text = item.title
        holder.mOutputContentText.text = item.content

        //色変え
        holder.mOutputDateText.setTextColor(item.colorFrag)
        holder.mOutputTitleText.setTextColor(item.colorFrag)
        holder.mOutputContentText.setTextColor(item.colorFrag)

        holder.mOutputCard.setCardBackgroundColor(item.color)
        //クリック
        holder.mOutputCard.setOnClickListener {
            val editDialogView = activity.layoutInflater.inflate(R.layout.input_daze_layout, null)
            val alert = android.support.v7.app.AlertDialog.Builder(activity)
                .setView(editDialogView)
                .show()

            //上のタイトル
            editDialogView.dazeInputTextView.text = activity.getString(R.string.memoInputOptionChange)

            //エディットテキストに挿入
            editDialogView.titleInput.setText(holder.mOutputTitleText.text, TextView.BufferType.EDITABLE)
            editDialogView.contentInput.setText(holder.mOutputContentText.text, TextView.BufferType.EDITABLE)
            //色変更
            editDialogView.testDazeText.setTextColor(holder.mOutputContentText.textColors)//文字色
            editDialogView.testDazeCardView.setCardBackgroundColor(holder.mOutputCard.cardBackgroundColor)//背景
            color = holder.mOutputCard.cardBackgroundColor.defaultColor
            darkLight = holder.mOutputContentText.textColors.defaultColor
            editDialogView.dazeColorInputButton.setOnClickListener {

                val view_picker = activity.layoutInflater.inflate(R.layout.picker_diarog_layout, null)
                val alertDialog = android.support.v7.app.AlertDialog.Builder(activity)
                    .setView(view_picker)
                    .show()

                view_picker.colorCancelButton.setOnClickListener {
                    alertDialog.dismiss()
                }
                var colorInt = color
                view_picker.color_picker.setAlphaSliderVisible(true)
                view_picker.color_picker.setOnColorChangedListener {
                    colorInt = it
                }
                view_picker.colorOkButton.setOnClickListener {
                    color = colorInt
                    val red = Color.red(color)
                    val green = Color.green(color)
                    val blue = Color.blue(color)

                    Log.d("Color", "red:$red, green:$green, blue:$blue")
                    Log.d("Color", "color:$color")

                    editDialogView.testDazeCardView.setCardBackgroundColor(colorInt)
                    darkLight = if (red < 0xDD || blue < 0xDD || green < 0xDD) {
                        editDialogView.testDazeText.setTextColor(activity.resources.getColor(R.color.darkText))
                        activity.resources.getColor(R.color.darkText)
                    } else {
                        editDialogView.testDazeText.setTextColor(activity.resources.getColor(R.color.lightText))
                        activity.resources.getColor(R.color.lightText)
                    }
                    alertDialog.dismiss()
                }
            }
            editDialogView.inputButton.setOnClickListener {
                val id = item.id
                val changeTitle = editDialogView.titleInput.text.toString()
                val changeContent = editDialogView.contentInput.text.toString()
                val changeColor = color
                val changeTextColor = darkLight

                //内容変更
                holder.mOutputTitleText.text = changeTitle//題名
                holder.mOutputContentText.text = changeContent//内容

                //色変え
                holder.mOutputCard.setCardBackgroundColor(color)//背景
                //文字
                holder.mOutputDateText.setTextColor(darkLight)
                holder.mOutputTitleText.setTextColor(darkLight)
                holder.mOutputContentText.setTextColor(darkLight)


                Completable.fromAction {
                    dataBase.userDao().editDaze(id, changeTitle, changeContent, changeColor, changeTextColor)
                }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
                alert.dismiss()
            }

        }

        //長押し
        holder.mOutputCard.setOnLongClickListener {

            AlertDialog.Builder(activity)
                .setTitle("削除します")
                .setMessage("「${item.title}」\nを削除します\nよろしいですか？")
                .setPositiveButton("OK") { _, _ ->
                    // OK button pressed
                    Completable.fromAction { dataBase.userDao().deleteId(item.id) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                    holder.mOutputCard.visibility = View.GONE
                }

                .setNegativeButton("Cancel", null)
                .show()
            true
        }
    }

    fun isDataBase(db: AppDataBase) {
        dataBase = db
    }


    fun isActivity(act: Activity) {
        activity = act
    }


    override fun getItemCount(): Int {
        return mValues.size
    }
}