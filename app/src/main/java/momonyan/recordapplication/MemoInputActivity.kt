package momonyan.recordapplication

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.input_memo_layout.*
import kotlinx.android.synthetic.main.picker_diarog_layout.view.*
import momonyan.recordapplication.memo_database.AppMemoDataBase
import momonyan.recordapplication.memo_database.Memo

class MemoInputActivity : AppCompatActivity() {

    private var color = 0xFFFFFF
    private var darkLight = 0xFFFFFF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_memo_layout)

        color = resources.getColor(R.color.darkText)
        darkLight = resources.getColor(R.color.lightText)

        //
        val dataBase =
            Room.databaseBuilder(applicationContext, AppMemoDataBase::class.java, "MemoDataBase.db")
                .build()

        // データモデルを作成
        val memo = Memo()

        memoButton.setOnClickListener {
            Log.d("Test", "YES")
            memo.check = false
            memo.content = memoInput.text.toString()
            memo.color = color
            memo.textColor = darkLight
            Completable.fromAction { dataBase.memoDao().insert(memo) }
                .subscribeOn(Schedulers.io())
                .subscribe()

            val intent = Intent(this, MainTabActivity::class.java)
            intent.putExtra("Position", 1)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            finish()
            startActivity(intent)
        }
        changeMemoBackButton.setOnClickListener {

            val view = layoutInflater.inflate(R.layout.picker_diarog_layout, null)
            val alert = AlertDialog.Builder(this)
                .setView(view)
                .show()

            view.colorCancelButton.setOnClickListener {
                alert.dismiss()
            }
            var colorInt = 0xFFFFFF
            view.color_picker.setAlphaSliderVisible(true)
            view.color_picker.setOnColorChangedListener {
                colorInt = it
            }
            view.colorOkButton.setOnClickListener {
                color = colorInt
                val red = Color.red(color)
                val green = Color.green(color)
                val blue = Color.blue(color)

                Log.d("Color", "red:$red, green:$green, blue:$blue")
                Log.d("Color", "color:$color")

                testMemoCardView.setCardBackgroundColor(colorInt)

                //ソフトキーボードを非表示
                if (view != null) {
                    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    view.clearFocus()
                }

                alert.dismiss()
                if (red < 0xDD || blue < 0xDD || green < 0xDD) {
                    testMemoText.setTextColor(resources.getColor(R.color.darkText))
                    darkLight = resources.getColor(R.color.darkText)
                } else {
                    testMemoText.setTextColor(resources.getColor(R.color.lightText))
                    darkLight = resources.getColor(R.color.lightText)
                }
            }
        }
    }
}