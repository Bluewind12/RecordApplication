package momonyan.recordapplication.memo

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import kotlinx.android.synthetic.main.memo_card_layout.view.*

class MemoHolder(mView: View) : RecyclerView.ViewHolder(mView) {
    val mIdTextView: TextView = mView.memoIdTextView   //Id表示
    val mMemoCheck: CheckBox = mView.memoCheckBox   //check
    val mContentTextView: TextView = mView.memoContentView   //内容

    val mMemoCardView: CardView = mView.memoCardView    //カードビュー
}