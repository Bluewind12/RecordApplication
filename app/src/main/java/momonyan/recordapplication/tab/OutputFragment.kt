package momonyan.recordapplication.tab

import android.arch.lifecycle.Observer
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tab_tab1_output_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.daze_database.AppDataBase
import momonyan.recordapplication.daze_database.User
import momonyan.recordapplication.daze_output.OutputAdapter
import momonyan.recordapplication.daze_output.OutputDataClass
import java.util.*


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
    private var tagMutableList: MutableList<String> = mutableListOf()//Tag

    private var mDataList: ArrayList<OutputDataClass> = ArrayList()

    private lateinit var dataBase: AppDataBase
    private var position: Int = 0
    private var maxCard = 10
    private var maxUser = 0

    private lateinit var adapter: OutputAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLayout = inflater.inflate(R.layout.tab_tab1_output_layout, container, false)

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE User ADD tag TEXT")
            }
        }
        //
        dataBase =
            Room.databaseBuilder(activity!!.applicationContext, AppDataBase::class.java, "TestDataBase.db")
                .addMigrations(MIGRATION_1_2)
                .build()


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
            tagMutableList = mutableListOf() //テキストカラー

            // ユーザー一覧を取得した時やデータが変更された時に呼ばれる
            if (users != null) {
                maxUser = users.size
                if (0 > users.size - maxCard) {
                    maxCard = users.size
                }
                for (u in 0 until users.size) {
                    userIdsMutableList.add(users[u].userId)
                    dateMutableList.add(users[u].day!!)
                    titleMutableList.add(users[u].title!!)
                    contentMutableList.add(users[u].content!!)
                    colorMutableList.add(users[u].color)
                    colorFragMutableList.add(users[u].colorDL)
                    memoMutableList.add(users[u].memo)
                    tagMutableList.add(users[u].tag!!)
                }
                for (i in users.size - 1 downTo users.size - maxCard) {
                    mDataList.add(
                        OutputDataClass(
                            userIdsMutableList[i],
                            dateMutableList[i],
                            titleMutableList[i],
                            contentMutableList[i],
                            colorMutableList[i],
                            colorFragMutableList[i],
                            memoMutableList[i],
                            tagMutableList[i]
                        )
                    )
                }
                // Adapter作成
                adapter = OutputAdapter(mDataList)
                adapter.isDataBase(dataBase)
                adapter.isActivity(activity!!)

                // RecyclerViewにAdapterとLayoutManagerの設定
                viewLayout.tab1_recyclerView.adapter = adapter
                viewLayout.tab1_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                //ページング処理
                viewLayout.tab1_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                                if (maxCard != users.size) {
                                    //保存
                                    position =
                                        (viewLayout.tab1_recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                                    //追加更新
                                    val lastInt = maxCard
                                    maxCard += 10
                                    loadRecycler(lastInt)

                                }
                            }
                        }
                    }
                })

            }
        })


        viewLayout.outputSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //テキスト変更前
                Log.d("TextChange", "Before:$s")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //テキスト変更中
                Log.d("TextChange", "NOW:$s")

                val text = s.toString()
                searchRequest(text)
            }

            override fun afterTextChanged(s: Editable) {
                Log.d("TextChange", "END:$s")

                //テキスト変更後
            }
        })

        //データセット
        return viewLayout
    }

    private fun loadRecycler(last: Int) {
        if (0 > maxUser - maxCard) {
            maxCard = maxUser
        }
        for (i in maxUser - 1 - last downTo maxUser - maxCard) {
            mDataList.add(
                OutputDataClass(
                    userIdsMutableList[i],
                    dateMutableList[i],
                    titleMutableList[i],
                    contentMutableList[i],
                    colorMutableList[i],
                    colorFragMutableList[i],
                    memoMutableList[i],
                    tagMutableList[i]
                )
            )
        }

        // Adapter作成
        adapter = OutputAdapter(mDataList)
        adapter.isDataBase(dataBase)
        adapter.isActivity(activity!!)
        // RecyclerViewにAdapterとLayoutManagerの設定
        viewLayout.tab1_recyclerView.adapter = adapter
        viewLayout.tab1_recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //移動
        (viewLayout.tab1_recyclerView.layoutManager as LinearLayoutManager).scrollToPosition(position)

    }

    fun searchRequest(text: String) {
        val adapter = adapter
        val regex = Regex(".*$text.*")
        adapter.mValues = mDataList.filter {
            Log.d("TEXT_CHECK", "${it.title} : $text")
            it.title.matches(regex) || it.content.matches(regex) || it.memo.matches(regex) || it.tag.matches(regex)
        }
        adapter.notifyDataSetChanged()
    }
}
