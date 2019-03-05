package momonyan.recordapplication.memo

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
import kotlinx.android.synthetic.main.input_memo_layout.view.*
import kotlinx.android.synthetic.main.picker_diarog_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.memo_database.AppMemoDataBase


class MemoAdapter(private val mValues: ArrayList<MemoDataClass>) : RecyclerView.Adapter<MemoHolder>() {
    private lateinit var dataBase: AppMemoDataBase
    private lateinit var activity: Activity

    private var color = 0xFFFFFF
    private var darkLight = 0xFFFFFF
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        //レイアウトの設定(inflate)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.memo_card_layout, parent, false)
        //Holderの生成
        return MemoHolder(view)
    }

    override fun onBindViewHolder(holder: MemoHolder, position: Int) {
        val item = mValues[position]
        holder.mIdTextView.text = item.id.toString()
        holder.mMemoCheck.isChecked = item.check
        holder.mContentTextView.text = item.content


        holder.mIdTextView.setTextColor(item.textColor)
        holder.mContentTextView.setTextColor(item.textColor)

        holder.mMemoCardView.setCardBackgroundColor(item.backColor)
        holder.itemView.setOnClickListener {
            val view = activity.layoutInflater.inflate(R.layout.input_memo_layout, null)
            val alert = android.support.v7.app.AlertDialog.Builder(activity)
                .setView(view)
                .show()

            view.memoInputTextView.text = activity.getString(R.string.memoInputOptionChange)

            view.memoInput.setText(holder.mContentTextView.text, TextView.BufferType.EDITABLE)
            view.testMemoCardView.setCardBackgroundColor(holder.mMemoCardView.cardBackgroundColor)
            color = holder.mMemoCardView.cardBackgroundColor.defaultColor
            view.testMemoText.setTextColor(holder.mContentTextView.textColors)

            view.changeMemoBackButton.setOnClickListener {

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

                    view.testMemoCardView.setCardBackgroundColor(colorInt)
                    if (red < 0xDD || blue < 0xDD || green < 0xDD) {
                        view.testMemoText.setTextColor(activity.resources.getColor(R.color.darkText))
                        darkLight = activity.resources.getColor(R.color.darkText)
                    } else {
                        view.testMemoText.setTextColor(activity.resources.getColor(R.color.lightText))
                        darkLight = activity.resources.getColor(R.color.lightText)
                    }
                    alertDialog.dismiss()
                }
            }
            view.memoButton.setOnClickListener {
                holder.mContentTextView.text = view.memoInput.text.toString()
                holder.mMemoCardView.setCardBackgroundColor(color)
                holder.mIdTextView.setTextColor(darkLight)
                holder.mContentTextView.setTextColor(darkLight)

                val id = item.id
                val changeContent = view.memoInput.text.toString()
                val changeColor = color
                val changeTextColor = darkLight

                Completable.fromAction {
                    dataBase.memoDao().updateMemoData(id, changeContent, changeColor, changeTextColor)
                }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
                alert.dismiss()
            }

        }
        holder.itemView.setOnLongClickListener {

            AlertDialog.Builder(activity)
                .setTitle("削除します")
                .setMessage("「${item.content}」\nを削除します\nよろしいですか？")
                .setPositiveButton("OK") { _, _ ->
                    // OK button pressed
                    Completable.fromAction { dataBase.memoDao().deleteId(item.id) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                    holder.mMemoCardView.visibility = View.GONE
                    holder.mMemoCheck.visibility = View.GONE
                }
                .setNegativeButton("Cancel", null)
                .show()

            true
        }

        holder.mMemoCheck.setOnClickListener {
            Completable.fromAction { dataBase.memoDao().updateMemoCheck(item.id, holder.mMemoCheck.isChecked) }
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }

    fun isDataBase(db: AppMemoDataBase) {
        dataBase = db
    }

    fun isActivity(act: Activity) {
        activity = act
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}