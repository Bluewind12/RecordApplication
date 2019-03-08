package momonyan.recordapplication

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.facebook.stetho.Stetho
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.input_daze_layout.*
import kotlinx.android.synthetic.main.picker_diarog_layout.view.*
import momonyan.recordapplication.daze_database.AppDataBase
import momonyan.recordapplication.daze_database.User
import java.text.SimpleDateFormat
import java.util.*


class DazeInputActivity : AppCompatActivity() {

    private var color = 0xFFFFFF
    private var darkLight = 0xFFFFFF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_daze_layout)

        color = resources.getColor(R.color.darkText)
        darkLight = resources.getColor(R.color.lightText)

        //Stetho
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        )
        //
        val dataBase =
            Room.databaseBuilder(applicationContext, AppDataBase::class.java, "TestDataBase.db")
                .build()

        // データモデルを作成
        val user = User()

        inputButton.setOnClickListener {
            Log.d("Test", "YES")
            user.title = titleInput.text.toString()
            user.content = contentInput.text.toString()
            user.day = getToday()
            user.color = color
            user.colorDL = darkLight
            Completable.fromAction { dataBase.userDao().insert(user) }
                .subscribeOn(Schedulers.io())
                .subscribe()

            val intent = Intent(this, MainTabActivity::class.java)
            intent.putExtra("Position", 0)
            finish()
            startActivity(intent)

        }

        dazeColorInputButton.setOnClickListener {
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

                testDazeCardView.setCardBackgroundColor(colorInt)

                //ソフトキーボードを非表示
                if (view != null) {
                    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    view.clearFocus()
                }

                alert.dismiss()
                darkLight = if (red < 0xDD || blue < 0xDD || green < 0xDD) {
                    testDazeText.setTextColor(resources.getColor(R.color.darkText))
                    resources.getColor(R.color.darkText)
                } else {
                    testDazeText.setTextColor(resources.getColor(R.color.lightText))
                    resources.getColor(R.color.lightText)
                }
            }
        }

    }

    private fun getToday(): String {
        val date = Date()
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        return format.format(date)
    }
}