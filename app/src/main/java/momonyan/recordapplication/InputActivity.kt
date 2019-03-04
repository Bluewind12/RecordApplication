package momonyan.recordapplication

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.stetho.Stetho
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.input_layout.*
import kotlinx.android.synthetic.main.picker_diarog_layout.view.*
import momonyan.recordapplication.daze_database.AppDataBase
import momonyan.recordapplication.daze_database.User
import java.text.SimpleDateFormat
import java.util.*


class InputActivity : AppCompatActivity() {

    var color = 0xFFFFFF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_layout)


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
            Completable.fromAction { dataBase.userDao().insert(user) }
                .subscribeOn(Schedulers.io())
                .subscribe()

            val intent = Intent(this, MainTabActivity::class.java)
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
                dazeCardView.setCardBackgroundColor(colorInt)
                alert.dismiss()
            }
        }

    }

    private fun getToday(): String {
        val date = Date()
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        return format.format(date)
    }
}