package momonyan.recordapplication.tab

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tab_tab1_output_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.daze_database.AppDataBase
import momonyan.recordapplication.daze_database.User
import momonyan.recordapplication.daze_output.OutputAdapter
import momonyan.recordapplication.daze_output.OutputDataClass

class OutputFragment : Fragment() {
    //表示用レイアウト
    private lateinit var viewLayout: View
    private var userIdsMutableList: MutableList<Int> = mutableListOf()//ID
    private var dateMutableList: MutableList<String> = mutableListOf()//日付
    private var titleMutableList: MutableList<String> = mutableListOf()//題名
    private var contentMutableList: MutableList<String> = mutableListOf()//内容
    private var colorMutableList: MutableList<Int> = mutableListOf()//色
    private var colorFragMutableList: MutableList<Int> = mutableListOf()//TextColor
    private var memoMutableList: MutableList<String> = mutableListOf()//Memo

    private var mDataList: ArrayList<OutputDataClass> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLayout = inflater.inflate(R.layout.tab_tab1_output_layout, container, false)

        //
        val dataBase =
            Room.databaseBuilder(activity!!.applicationContext, AppDataBase::class.java, "TestDataBase.db")
                .build()

        var frag = true
        dataBase.userDao().getAll().observe(this, Observer<List<User>> { users ->


            // データの初期化
            mDataList = arrayListOf() //まとめ

            userIdsMutableList = mutableListOf() //Id
            dateMutableList = mutableListOf() //日付
            titleMutableList = mutableListOf() //題名
            contentMutableList = mutableListOf() //内容
            colorMutableList = mutableListOf() //背景
            colorFragMutableList = mutableListOf() //テキストカラー
            memoMutableList = mutableListOf() //テキストカラー

            // ユーザー一覧を取得した時やデータが変更された時に呼ばれる
            if (users != null && frag) {
                for (u in 0 until users.size) {
                    userIdsMutableList.add(users[u].userId)
                    dateMutableList.add(users[u].day!!)
                    titleMutableList.add(users[u].title!!)
                    contentMutableList.add(users[u].content!!)
                    colorMutableList.add(users[u].color)
                    colorFragMutableList.add(users[u].colorDL)
                    memoMutableList.add(users[u].memo)
                }
                for (i in 0 until dateMutableList.size) {
                    mDataList.add(
                        OutputDataClass(
                            userIdsMutableList[i],
                            dateMutableList[i],
                            titleMutableList[i],
                            contentMutableList[i],
                            colorMutableList[i],
                            colorFragMutableList[i],
                            memoMutableList[i]
                        )
                    )
                }
                //mDataListの逆順へ
                mDataList.reverse()


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