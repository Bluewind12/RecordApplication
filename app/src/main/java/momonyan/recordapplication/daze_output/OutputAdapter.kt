package momonyan.recordapplication.daze_output

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import momonyan.recordapplication.DetailOutputActivity
import momonyan.recordapplication.R
import momonyan.recordapplication.daze_database.AppDataBase


class OutputAdapter(var mValues: List<OutputDataClass>) : RecyclerView.Adapter<OutputHolder>() {
    private lateinit var dataBase: AppDataBase
    private lateinit var activity: Activity
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

        //色変え
        holder.mOutputDateText.setTextColor(item.colorFrag)
        holder.mOutputTitleText.setTextColor(item.colorFrag)
        holder.mOutputContentText.setTextColor(item.colorFrag)

        holder.mOutputCard.setCardBackgroundColor(item.color)
        //クリック
        holder.mOutputCard.setOnClickListener {
            val detailIntent = Intent(activity, DetailOutputActivity::class.java)
            //
            //データ
            detailIntent.putExtra("Id", item.id)
            detailIntent.putExtra("Title", item.title)
            detailIntent.putExtra("Date", item.date)
            detailIntent.putExtra("Content", item.content)
            detailIntent.putExtra("BackColor", item.color)
            detailIntent.putExtra("TextColor", item.colorFrag)
            detailIntent.putExtra("Memo", item.memo)
            detailIntent.putExtra("Tag", item.tag)


            activity.startActivityForResult(detailIntent, 0)
        }

        //長押し
        holder.mOutputCard.setOnLongClickListener {

            AlertDialog.Builder(activity)
                .setTitle("削除します")
                .setMessage("「${item.title}」\nを削除します\nよろしいですか？")
                .setPositiveButton("OK") { _, _ ->
                    // OK button pressed
                    Completable.fromAction { dataBase.userDao().deleteId(item.id) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
                    holder.mOutputCard.visibility = View.GONE
                }

                .setNegativeButton("Cancel", null)
                .show()
            true
        }
    }

    fun isDataBase(db: AppDataBase) {
        dataBase = db
    }


    fun isActivity(act: Activity) {
        activity = act
    }


    override fun getItemCount(): Int {
        return mValues.size
    }

}