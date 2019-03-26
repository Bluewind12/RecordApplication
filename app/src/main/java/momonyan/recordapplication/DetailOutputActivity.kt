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
    private var date: String = ""
    private var backgroundColor: Int = -1
    private var textColor: Int = -1

    private var memo: String = ""
    private var tag: String = ""

    private var deleteFrag = false

    private lateinit var dataBase: AppDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_output_layout)

        //パラメータ取得
        id = intent.getIntExtra("Id", -1)
        title = intent.getStringExtra("Title")
        content = intent.getStringExtra("Content")
        date = intent.getStringExtra("Date")
        backgroundColor = intent.getIntExtra("BackColor", -1)
        textColor = intent.getIntExtra("TextColor", -1)

        memo = intent.getStringExtra("Memo")
        tag = intent.getStringExtra("Tag")

        //レイアウト設置
        detailTitleText.text = title
        detailContentText.text = content
        dazeMemoTextView.text = memo

        val tagList = tag.split(",")
        tagList.forEach {
            if (it != "") {
                val textView = TextView(this)
                textView.text = it
                textView.background = getDrawable(R.drawable.background_tag_text_light)
                tagLinearLayout.addView(textView)
                val pattingText = TextView(this)
                pattingText.text = " "
                tagLinearLayout.addView(pattingText)
            }
        }
        //色
        detailTitle.setTextColor(textColor)
        detailTitleText.setTextColor(textColor)
        detailContent.setTextColor(textColor)
        detailContentText.setTextColor(textColor)
        detailCardView.setCardBackgroundColor(backgroundColor)

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
                editDialogView.titleInput.setText(title, TextView.BufferType.EDITABLE) //題名
                editDialogView.contentInput.setText(content, TextView.BufferType.EDITABLE) //内容
                editDialogView.dazeMemoEditText.setText(memo, TextView.BufferType.EDITABLE) //メモ
                editDialogView.dazeTagEditText.setText(tag, TextView.BufferType.EDITABLE) //Tag
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
                    val changeMemo = editDialogView.dazeMemoEditText.text.toString()
                    val changeTag = editDialogView.dazeTagEditText.text.toString()
                    val changeColor = backgroundColor
                    val changeTextColor = textColor

                    //内容変更
                    detailTitleText.text = changeTitle//題名
                    detailContentText.text = changeContent//内容
                    dazeMemoTextView.text = changeMemo//Memo


                    //Tag
                    tagLinearLayout.removeAllViews()
                    val tagList = changeTag.split(",")
                    tagList.forEach {
                        if (it != "") {
                            val textView = TextView(this)
                            textView.text = it
                            textView.setTextColor(changeTextColor)
                            if (changeTextColor == R.color.lightText) {
                                //白ベース
                                textView.background = getDrawable(R.drawable.background_tag_text_light)
                            } else {
                                //黒ベース
                                textView.background = getDrawable(R.drawable.background_tag_text_dark)
                            }
                            tagLinearLayout.addView(textView)
                            val pattingText = TextView(this)
                            pattingText.text = " "
                            tagLinearLayout.addView(pattingText)
                        }
                    }
                    //文字
                    detailTitle.setTextColor(textColor)
                    detailTitleText.setTextColor(textColor)
                    detailContent.setTextColor(textColor)
                    detailContentText.setTextColor(textColor)

                    //色変え
                    detailCardView.setCardBackgroundColor(backgroundColor)//背景
                    Completable.fromAction {
                        dataBase.userDao().upDateDaze(
                            id,
                            changeTitle,
                            changeContent,
                            changeColor,
                            changeTextColor,
                            changeMemo,
                            changeTag
                        )
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
            R.id.detailMenu3 -> {
                val shareStringData = getString(R.string.shareFormat, date, title, content)
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, shareStringData)
                startActivity(intent)
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
        setResult(Activity.RESULT_OK)
    }

}