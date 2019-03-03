package momonyan.recordapplication.memo

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import momonyan.recordapplication.R
import momonyan.recordapplication.memo_database.AppMemoDataBase


class MemoAdapter(private val mValues: ArrayList<MemoDataClass>) : RecyclerView.Adapter<MemoHolder>() {
    private lateinit var dataBase: AppMemoDataBase
    private lateinit var handler: Handler

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        //レイアウトの設定(inflate)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.memo_card_layout, parent, false)
        //Holderの生成
        return MemoHolder(view)
    }

    override fun onBindViewHolder(holder: MemoHolder, position: Int) {
        val item = mValues[position]
        holder.mIdTextView.text = item.id.toString()
        holder.mMemoCheck.isChecked = item.check
        holder.mContentTextView.text = item.content
        holder.itemView.setOnLongClickListener {
            Completable.fromAction { dataBase.memoDao().deleteId(item.id) }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    // Handlerを使用してメイン(UI)スレッドに処理を依頼する
                    handler.post { holder.mMemoCardView.visibility = View.GONE }
                }

            true
        }
    }

    fun isDataBase(db: AppMemoDataBase) {
        dataBase = db
    }

    fun isHandler(hn: Handler) {
        handler = hn
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}