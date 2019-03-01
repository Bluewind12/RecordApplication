package momonyan.recordapplication

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.stetho.Stetho
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.input_layout.*
import momonyan.recordapplication.database.AppDataBase
import momonyan.recordapplication.database.User

class InputActivity : AppCompatActivity() {
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
            user.name = nameInput.text.toString()
            user.info = infoInput.text.toString()
            if (ageInput.text.toString() != "") {
                user.age = ageInput.text.toString().toInt()
            }
            Completable.fromAction { val id = dataBase.userDao().insert(user) }
                .subscribeOn(Schedulers.io())
                .subscribe()

            val intent = Intent(this, MainTabActivity::class.java)
            finish()
            startActivity(intent)

        }

    }
}