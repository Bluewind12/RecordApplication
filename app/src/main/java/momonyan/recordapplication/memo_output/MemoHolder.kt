package momonyan.recordapplication.output

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import kotlinx.android.synthetic.main.memo_card_layout.view.*

class MemoHolder(mView: View) : RecyclerView.ViewHolder(mView) {
    val mMemoCheck: CheckBox = mView.memoCheckBox   //題名
    val mMemoContentText: TextView = mView.memoContentView   //内容

    val mMemoVard: CardView = mView.memoCardView    //カードビュー
}