package momonyan.recordapplication.tab

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.tab_tab1_output_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.daze_database.AppDataBase
import momonyan.recordapplication.daze_database.User
import momonyan.recordapplication.daze_output.MemoAdapter
import momonyan.recordapplication.daze_output.MemoDataClass

class OutputFragment : Fragment() {
    //表示用レイアウト
    private lateinit var viewLayout: View
    private var dateMutableList: MutableList<String> = mutableListOf()
    private var titleMutableList: MutableList<String> = mutableListOf()
    private var contentMutableList: MutableList<String> = mutableListOf()

    private var mDataList: ArrayList<MemoDataClass> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLayout = inflater.inflate(R.layout.tab_tab1_output_layout, container, false)

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


//        Completable.fromAction { val id = dataBase.userDao().getAll() }
//            .subscribeOn(Schedulers.io())
//            .subscribe()

        dataBase.userDao().getAll().observe(this, Observer<List<User>> { users ->
            // ユーザー一覧を取得した時やデータが変更された時に呼ばれる
            if (users != null) {
                // TODO ユーザー一覧をRecyclerViewなどで表示
                Log.d("TestTags","TestMan")
                for (u in 0 until users.size) {
                    dateMutableList.add(users[u].userId.toString())
                    titleMutableList.add(users[u].name!!)
                    contentMutableList.add(users[u].info!!)
                }

                // データ作成
                if (mDataList.isEmpty()) {
                    for (i in 0 until dateMutableList.size) {
                        mDataList.add(
                            MemoDataClass(
                                dateMutableList[i],
                                titleMutableList[i],
                                contentMutableList[i]
                            )
                        )
                        Log.d("TabDataSet", "Tab1:DataNum $i")
                    }
                }
                // Adapter作成
                val adapter = MemoAdapter(mDataList)

                // RecyclerViewにAdapterとLayoutManagerの設定
                viewLayout.tab1_recyclerView.adapter = adapter
                viewLayout.tab1_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        })

        //データセット


        return viewLayout
    }
}