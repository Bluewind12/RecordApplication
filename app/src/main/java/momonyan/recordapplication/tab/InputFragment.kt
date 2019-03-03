package momonyan.recordapplication.tab

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.stetho.Stetho
import io.reactivex.Completable.fromAction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.input_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.daze_database.AppDataBase
import momonyan.recordapplication.daze_database.User

class InputFragment : Fragment() {

    private lateinit var viewLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLayout = inflater.inflate(R.layout.input_layout, container, false)

        //Stetho
        Stetho.initialize(
            Stetho.newInitializerBuilder(context)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                .build()
        )
        //
        val dataBase =
            Room.databaseBuilder(activity!!.applicationContext, AppDataBase::class.java, "TestDataBase.db")
                .build()

        // データモデルを作成
        val user = User()

        viewLayout.inputButton.setOnClickListener {
            Log.d("Test", "YES")
            user.title = viewLayout.titleInput.text.toString()
            user.content = viewLayout.contentInput.text.toString()
            fromAction { dataBase.userDao().insert(user) }
                .subscribeOn(Schedulers.io())
                .subscribe()
        }

        return viewLayout
    }
}