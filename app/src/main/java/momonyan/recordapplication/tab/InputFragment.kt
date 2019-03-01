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
import kotlinx.android.synthetic.main.tab_tab3_input_layout.*
import kotlinx.android.synthetic.main.tab_tab3_input_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.database.AppDataBase
import momonyan.recordapplication.database.User

class InputFragment : Fragment() {

    private lateinit var viewLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLayout = inflater.inflate(R.layout.tab_tab3_input_layout, container, false)

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
            user.name = viewLayout.nameInput.text.toString()
            user.info = viewLayout.infoInput.text.toString()
            if (viewLayout.ageInput.text.toString() != "") {
                user.age = viewLayout.ageInput.text.toString().toInt()
            }
            fromAction { val id = dataBase.userDao().insert(user) }
                .subscribeOn(Schedulers.io())
                .subscribe()


        }

        return viewLayout
    }
}