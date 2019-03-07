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
import kotlinx.android.synthetic.main.tab_tab2_memo_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.memo.MemoAdapter
import momonyan.recordapplication.memo.MemoDataClass
import momonyan.recordapplication.memo_database.AppMemoDataBase
import momonyan.recordapplication.memo_database.Memo


class MemoFragment : Fragment() {
    private var mDataList: ArrayList<MemoDataClass> = ArrayList()

    private var idMutableList: MutableList<Int> = mutableListOf()//id
    private var booleanMutableList: MutableList<Boolean> = mutableListOf()//チェック
    private var contentMutableList: MutableList<String> = mutableListOf()//内容
    private var colorMutableList: MutableList<Int> = mutableListOf()//背景
    private var textColorMutableList: MutableList<Int> = mutableListOf()//文字


    private lateinit var viewLayout: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLayout = inflater.inflate(R.layout.tab_tab2_memo_layout, container, false)

        //Stetho
        Stetho.initialize(
            Stetho.newInitializerBuilder(context)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context))
                .build()
        )

        //
        val dataBase =
            Room.databaseBuilder(activity!!.applicationContext, AppMemoDataBase::class.java, "MemoDataBase.db")
                .build()

        var frag = true

        dataBase.memoDao().getAll().observe(this, Observer<List<Memo>> { memos ->
            // ユーザー一覧を取得した時やデータが変更された時に呼ばれる

            // データ初期化
            mDataList = arrayListOf()
            idMutableList = mutableListOf()
            booleanMutableList = mutableListOf()
            contentMutableList = mutableListOf()
            colorMutableList = mutableListOf()
            textColorMutableList = mutableListOf()

            if (memos != null && frag) {
                // TODO ユーザー一覧をRecyclerViewなどで表示
                Log.d("TestTags", "TestMan!!!")
                for (u in 0 until memos.size) {
                    idMutableList.add(memos[u].memoId)
                    booleanMutableList.add(memos[u].check!!)
                    contentMutableList.add(memos[u].content!!)
                    colorMutableList.add(memos[u].color)
                    textColorMutableList.add(memos[u].textColor)
                }

                for (i in 0 until booleanMutableList.size) {
                    mDataList.add(
                        MemoDataClass(
                            idMutableList[i],
                            booleanMutableList[i],
                            contentMutableList[i],
                            colorMutableList[i],
                            textColorMutableList[i]
                        )
                    )
                }

                // Adapter作成
                val adapter = MemoAdapter(mDataList)
                adapter.isDataBase(dataBase)
                adapter.isActivity(activity!!)

                // RecyclerViewにAdapterとLayoutManagerの設定
                viewLayout.tab2_recyclerView.adapter = adapter
                viewLayout.tab2_recyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                frag = false
            }
        })
        return viewLayout
    }

}