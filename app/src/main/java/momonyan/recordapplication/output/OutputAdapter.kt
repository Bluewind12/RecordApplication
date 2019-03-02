package momonyan.recordapplication.output

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import momonyan.recordapplication.R

class OutputAdapter(private val mValues: ArrayList<OutputDataClass>) : RecyclerView.Adapter<OutputHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutputHolder {
        //レイアウトの設定(inflate)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.output_card_layout, parent, false)
        //Holderの生成
        return OutputHolder(view)
    }

    override fun onBindViewHolder(holder: OutputHolder, position: Int) {
        val item = mValues[position]
        holder.mOutputDateText.text = item.date
        holder.mOutputTitleText.text = item.title
        holder.mOutputContentText.text = item.content
    }

    override fun getItemCount(): Int {
        return mValues.size
    }
}