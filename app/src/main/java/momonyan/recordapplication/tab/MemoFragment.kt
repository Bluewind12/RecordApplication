package momonyan.recordapplication.tab

import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var dataBase: AppMemoDataBase
    private var position: Int = 0
    private var maxCard = 20
    private var memoMax = 0

    private lateinit var viewLayout: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLayout = inflater.inflate(R.layout.tab_tab2_memo_layout, container, false)
        //
        dataBase =
            Room.databaseBuilder(activity!!.applicationContext, AppMemoDataBase::class.java, "MemoDataBase.db")
                .build()

        dataBase.memoDao().getAll().observe(this, Observer<List<Memo>> { memos ->
            // ユーザー一覧を取得した時やデータが変更された時に呼ばれる

            // データ初期化
            mDataList = arrayListOf()
            idMutableList = mutableListOf()
            booleanMutableList = mutableListOf()
            contentMutableList = mutableListOf()
            colorMutableList = mutableListOf()
            textColorMutableList = mutableListOf()

            if (memos != null) {
                memoMax = memos.size
                if (0 > memoMax - maxCard) {
                    maxCard = memos.size
                }
                for (u in 0 until memos.size) {
                    idMutableList.add(memos[u].memoId)
                    booleanMutableList.add(memos[u].check!!)
                    contentMutableList.add(memos[u].content!!)
                    colorMutableList.add(memos[u].color)
                    textColorMutableList.add(memos[u].textColor)
                }

                for (i in memoMax - 1 downTo memoMax - maxCard) {
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

                //ページング処理
                viewLayout.tab2_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val totalCount = recyclerView.adapter!!.itemCount //合計のアイテム数
                        val childCount = recyclerView.childCount // RecyclerViewに表示されてるアイテム数
                        val layoutManager = recyclerView.layoutManager

                        if (layoutManager is LinearLayoutManager) { // LinearLayoutManager
                            val linearLayoutManager = layoutManager as LinearLayoutManager?
                            val firstPosition =
                                linearLayoutManager!!.findFirstVisibleItemPosition() // RecyclerViewの一番上に表示されているアイテムのポジション
                            if (totalCount == childCount + firstPosition) {
                                // ページング処理
                                // LinearLayoutManagerを指定している時のページング処理
                                if (maxCard != memos.size) {
                                    //保存
                                    position =
                                        (viewLayout.tab2_recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                                    //追加更新
                                    val lastInt = maxCard
                                    maxCard += 20
                                    loadRecycler(lastInt)

                                }
                            }
                        }
                    }
                })

            }
        })

        //データセット
        return viewLayout
    }

    private fun loadRecycler(last: Int) {
        if (0 > memoMax - maxCard) {
            maxCard = memoMax
        }

        for (i in memoMax - 1 - last downTo memoMax - maxCard) {
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

        //移動
        (viewLayout.tab2_recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(position)
    }
}