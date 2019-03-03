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
import momonyan.recordapplication.daze_output.OutputAdapter
import momonyan.recordapplication.daze_output.OutputDataClass

class OutputFragment : Fragment() {
    //表示用レイアウト
    private lateinit var viewLayout: View
    private var userIdsMutableList: MutableList<Int> = mutableListOf()
    private var dateMutableList: MutableList<String> = mutableListOf()
    private var titleMutableList: MutableList<String> = mutableListOf()
    private var contentMutableList: MutableList<String> = mutableListOf()

    private var mDataList: ArrayList<OutputDataClass> = ArrayList()

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

        var frag = true
        dataBase.userDao().getAll().observe(this, Observer<List<User>> { users ->
            // ユーザー一覧を取得した時やデータが変更された時に呼ばれる
            if (users != null && frag) {
                // TODO ユーザー一覧をRecyclerViewなどで表示
                Log.d("TestTags","TestMan")
                for (u in 0 until users.size) {
                    userIdsMutableList.add(users[u].userId)
                    dateMutableList.add(users[u].day!!)
                    titleMutableList.add(users[u].title!!)
                    contentMutableList.add(users[u].content!!)
                }

                // データ作成
                if (mDataList.isEmpty()) {
                    for (i in 0 until dateMutableList.size) {
                        mDataList.add(
                            OutputDataClass(
                                userIdsMutableList[i],
                                dateMutableList[i],
                                titleMutableList[i],
                                contentMutableList[i]
                            )
                        )
                        Log.d("TabDataSet", "Tab1:DataNum $i")
                    }
                }
                // Adapter作成
                val adapter = OutputAdapter(mDataList)
                adapter.isDataBase(dataBase)
                adapter.isActivity(activity!!)

                // RecyclerViewにAdapterとLayoutManagerの設定
                viewLayout.tab1_recyclerView.adapter = adapter
                viewLayout.tab1_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                frag = false
            }
        })

        //データセット


        return viewLayout
    }
}