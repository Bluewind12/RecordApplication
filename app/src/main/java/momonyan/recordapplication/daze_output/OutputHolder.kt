package momonyan.recordapplication.daze_output

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.output_card_layout.view.*

class OutputHolder(mView: View) : RecyclerView.ViewHolder(mView) {
    val mOutputDateText: TextView = mView.dateTextView   //日付
    val mOutputTitleText: TextView = mView.titleTextView   //題名
    val mOutputContentText: TextView = mView.contentTextView   //内容

    val mOutputCard: CardView = mView.outputCardView    //カードビュー
}