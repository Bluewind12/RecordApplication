package momonyan.recordapplication.output

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import momonyan.recordapplication.R

class MemoAdapter(private val mValues: ArrayList<MemoDataClass>) : RecyclerView.Adapter<MemoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        //レイアウトの設定(inflate)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.output_card_layout, parent, false)
        //Holderの生成
        return MemoHolder(view)
    }

    override fun onBindViewHolder(holder: MemoHolder, position: Int) {
        val item = mValues[position]
        holder.mMemoCheck.text = item.title
        holder.mMemoContentText.text = item.content
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}