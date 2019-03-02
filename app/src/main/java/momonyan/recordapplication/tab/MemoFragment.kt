package momonyan.recordapplication.tab

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.stetho.Stetho
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.input_layout.view.*
import kotlinx.android.synthetic.main.memo_card_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.memo_database.AppMemoDataBase
import momonyan.recordapplication.memo_database.Memo

class MemoFragment: Fragment(){

    private lateinit var viewLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLayout = inflater.inflate(R.layout.memo_card_layout, container, false)

        //Stetho
        Stetho.initialize(
            Stetho.newInitializerBuilder(context)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                .build()
        )
        //
        val dataBase =
            Room.databaseBuilder(activity!!.applicationContext, AppMemoDataBase::class.java, "memoDataBase.db")
                .build()

        // データモデルを作成
        val memo = Memo()

        viewLayout.inputButton.setOnClickListener {
            Log.d("debug", "Memo")
            memo.check = viewLayout.memoCheckBox.isChecked
            memo.content = viewLayout.memoContentView.text.toString()
            Completable.fromAction { val id = dataBase.memoDao().insert(memo) }
                .subscribeOn(Schedulers.io())
                .subscribe()


        }

        return viewLayout
    }

}