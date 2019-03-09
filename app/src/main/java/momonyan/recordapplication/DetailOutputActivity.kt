package momonyan.recordapplication

import android.app.Activity
import android.app.AlertDialog
import android.arch.persistence.room.Room
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.details_output_layout.*
import kotlinx.android.synthetic.main.input_daze_layout.view.*
import kotlinx.android.synthetic.main.picker_diarog_layout.view.*
import momonyan.recordapplication.daze_database.AppDataBase

class DetailOutputActivity : AppCompatActivity() {
    private var id: Int = -1
    private var title: String = ""
    private var content: String = ""
    private var backgroundColor: Int = -1
    private var textColor: Int = -1

    private var deleteFrag = false

    private lateinit var dataBase: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_output_layout)

        //パラメータ取得
        id = intent.getIntExtra("Id", -1)
        title = intent.getStringExtra("Title")
        content = intent.getStringExtra("Content")
        backgroundColor = intent.getIntExtra("BackColor", -1)
        textColor = intent.getIntExtra("TextColor", -1)

        //レイアウト設置
        detailTitleText.text = title
        detailContentText.text = content
        //色
        detailTitle.setTextColor(textColor)
        detailTitleText.setTextColor(textColor)
        detailContent.setTextColor(textColor)
        detailContentText.setTextColor(textColor)
        detailsCardView.setCardBackgroundColor(backgroundColor)

        //DB
        dataBase =
            Room.databaseBuilder(applicationContext, AppDataBase::class.java, "TestDataBase.db")
                .build()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.detailMenu1 -> {
                val editDialogView = layoutInflater.inflate(R.layout.input_daze_layout, null)
                val alert = android.support.v7.app.AlertDialog.Builder(this)
                    .setView(editDialogView)
                    .show()

                //上のタイトル
                editDialogView.dazeInputTextView.text = getString(R.string.memoInputOptionChange)

                //エディットテキストに挿入
                editDialogView.titleInput.setText(title, TextView.BufferType.EDITABLE)  //題名
                editDialogView.contentInput.setText(content, TextView.BufferType.EDITABLE)  //内容
                //色変更
                editDialogView.testDazeText.setTextColor(textColor) //文字色
                editDialogView.testDazeCardView.setCardBackgroundColor(backgroundColor) //背景

                editDialogView.dazeColorInputButton.setOnClickListener {
                    val viewPicker = layoutInflater.inflate(R.layout.picker_diarog_layout, null)
                    val alertDialog = android.support.v7.app.AlertDialog.Builder(this)
                        .setView(viewPicker)
                        .show()

                    viewPicker.colorCancelButton.setOnClickListener {
                        alertDialog.dismiss()
                    }
                    var colorInt = backgroundColor
                    viewPicker.color_picker.setAlphaSliderVisible(true)
                    viewPicker.color_picker.setOnColorChangedListener {
                        colorInt = it
                    }
                    viewPicker.colorOkButton.setOnClickListener {
                        backgroundColor = colorInt
                        val red = Color.red(backgroundColor)
                        val green = Color.green(backgroundColor)
                        val blue = Color.blue(backgroundColor)

                        Log.d("Color", "red:$red, green:$green, blue:$blue")
                        Log.d("Color", "color:$backgroundColor")

                        editDialogView.testDazeCardView.setCardBackgroundColor(colorInt)
                        textColor = if (red < 0xDD || blue < 0xDD || green < 0xDD) {
                            editDialogView.testDazeText.setTextColor(resources.getColor(R.color.darkText))
                            resources.getColor(R.color.darkText)
                        } else {
                            editDialogView.testDazeText.setTextColor(resources.getColor(R.color.lightText))
                            resources.getColor(R.color.lightText)
                        }
                        alertDialog.dismiss()
                    }
                }
                editDialogView.inputButton.setOnClickListener {
                    val changeTitle = editDialogView.titleInput.text.toString()
                    val changeContent = editDialogView.contentInput.text.toString()
                    val changeColor = backgroundColor
                    val changeTextColor = textColor

                    //内容変更
                    detailTitleText.text = changeTitle//題名
                    detailContentText.text = changeContent//内容

                    //文字
                    detailTitle.setTextColor(textColor)
                    detailTitleText.setTextColor(textColor)
                    detailContent.setTextColor(textColor)
                    detailContentText.setTextColor(textColor)

                    //色変え
                    detailsCardView.setCardBackgroundColor(backgroundColor)//背景
                    Completable.fromAction {
                        dataBase.userDao().editDaze(id, changeTitle, changeContent, changeColor, changeTextColor)
                    }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                    alert.dismiss()
                }
            }
            R.id.detailMenu2 -> {
                AlertDialog.Builder(this)
                    .setTitle("削除します")
                    .setMessage("「${title}」\nを削除します\nよろしいですか？")
                    .setPositiveButton("OK") { _, _ ->
                        // OK button pressed
                        val intent = Intent()
                        intent.putExtra("delete", true)
                        Completable.fromAction { dataBase.userDao().deleteId(id) }
                            .subscribeOn(Schedulers.io())
                            .subscribe()
                        deleteFrag = true
                        finish()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            else -> error("対象外エラー１")
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu_tab, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!deleteFrag) {
            //デリート出なかった場合の処理（メモの書き込み処理）
        }
        setResult(Activity.RESULT_OK)
    }

}