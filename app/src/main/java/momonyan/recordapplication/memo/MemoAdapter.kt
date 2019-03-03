package momonyan.recordapplication.memo

import android.app.Activity
import android.app.AlertDialog
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
    private lateinit var activity: Activity

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

            AlertDialog.Builder(activity)
                .setTitle("削除します")
                .setMessage("「${item.content}」\nを削除します\nよろしいですか？")
                .setPositiveButton("OK") { _, _ ->
                    // OK button pressed
                    Completable.fromAction { dataBase.memoDao().deleteId(item.id) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                    holder.mMemoCardView.visibility = View.GONE
                }
                .setNegativeButton("Cancel", null)
                .show()

            true
        }
    }

    fun isDataBase(db: AppMemoDataBase) {
        dataBase = db
    }

    fun isActivity(act: Activity) {
        activity = act
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}