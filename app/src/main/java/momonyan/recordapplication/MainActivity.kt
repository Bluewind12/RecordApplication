package momonyan.recordapplication

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.facebook.stetho.Stetho
import io.reactivex.Completable.fromAction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import momonyan.recordapplication.daze_database.AppDataBase
import momonyan.recordapplication.daze_database.User

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        )

        val db = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "TestDataBase.db").build()

        // データモデルを作成
        val user = User()

        button.setOnClickListener {
            Log.d("Test", "YES")
            user.name = nameEditText.text.toString()
            user.info = infoEditText.text.toString()
            fromAction { val id = db.userDao().insert(user) }
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }
}
