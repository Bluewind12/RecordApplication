package momonyan.recordapplication.memo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import momonyan.recordapplication.R


class MemoAdapter(private val mValues: ArrayList<MemoDataClass>) : RecyclerView.Adapter<MemoHolder>() {

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
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}