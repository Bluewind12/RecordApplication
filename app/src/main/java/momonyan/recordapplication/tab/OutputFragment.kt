package momonyan.recordapplication.tab

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.stetho.Stetho
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.tab_tab1_output_layout.view.*
import momonyan.recordapplication.R
import momonyan.recordapplication.database.AppDataBase
import momonyan.recordapplication.database.User
import momonyan.recordapplication.output.OutputAdapter
import momonyan.recordapplication.output.OutputDataClass

class OutputFragment : Fragment() {
    //表示用レイアウト
    private lateinit var viewLayout: View
    private var dateMutableList: MutableList<String> = mutableListOf()
    private var titleMutableList: MutableList<String> = mutableListOf()
    private var contentMutableList: MutableList<String> = mutableListOf()

    private lateinit var userList: List<User>

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
        val database =
            Room.databaseBuilder(activity!!.applicationContext, AppDataBase::class.java, "kotlin_room_sample.db")
                .build()

//        userList = database.userDao().getAll()
        Single.fromCallable { database.userDao().getAll() }
            .subscribeOn(Schedulers.io())
            .flatMapObservable { it.toObservable() }
            .map {
                mDataList.add(OutputDataClass(it.userId.toString(), it.name!!, it.info!!))

                Log.d("Tag", "${it.userId.toString()}: ${it.name!!}: ${it.info!!}")
            }


        Completable.fromAction { val id = database.userDao().getAll() }
            .subscribeOn(Schedulers.io())
            .subscribe()

        //データセット
//        for (u in 0 until userList.size) {
//            dateMutableList.add(userList[u].userId.toString())
//            titleMutableList.add(userList[u].name!!)
//            contentMutableList.add(userList[u].info!!)
//        }

        // データ作成
        if (mDataList.isEmpty()) {
            for (i in 0 until dateMutableList.size) {
                mDataList.add(
                    OutputDataClass(
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

        // RecyclerViewにAdapterとLayoutManagerの設定
        viewLayout.tab1_recyclerView.adapter = adapter
        viewLayout.tab1_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return viewLayout
    }
}