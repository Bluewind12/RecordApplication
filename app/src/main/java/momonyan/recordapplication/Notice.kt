package momonyan.recordapplication

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat


class Notice : BroadcastReceiver() {

    override fun onReceive(content: Context, intent: Intent) {

        //通知がクリックされた時に発行されるIntentの生成
        val sendIntent = Intent(content, MainTabActivity::class.java)
        val sender = PendingIntent.getActivity(content, 0, sendIntent, 0)

        val message = "日記をとりませんか？"

        val icon = BitmapFactory.decodeResource(content.resources, R.mipmap.ic_launcher)

        //通知オブジェクトの生成
        val noti = NotificationCompat.Builder(content)
            .setTicker("日記をとりませんか？")
            .setContentTitle("メモ付き日記！")
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(icon)
            .setAutoCancel(true)
            .setContentIntent(sender)
            .build()

        val manager = content.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, noti)
    }
}