package momonyan.recordapplication

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import jp.co.runners.rateorfeedback.RateOrFeedback
import kotlinx.android.synthetic.main.tab_main_layout.*
import net.nend.android.NendAdInterstitial
import net.nend.android.NendAdInterstitialVideo
import java.util.*


class MainTabActivity : AppCompatActivity() {
    private lateinit var mSectionsPagerAdapter: TabAdapter
    private var frag: Int = 0
    private var tabPosition: Int = 0

    //menu
    private var menu0: MenuItem? = null
    private var menu1: MenuItem? = null
    private var menu2: MenuItem? = null
    //nend
    private lateinit var nendAdInterstitialVideo: NendAdInterstitialVideo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_main_layout)

        //Nend
        nendAdInterstitialVideo = NendAdInterstitialVideo(
            this,
            resources.getInteger(R.integer.ad_tab_movie_Id),
            getString(R.string.ad_tab_movie_Key)
        )
        nendAdInterstitialVideo.loadAd()

        NendAdInterstitial.loadAd(
            applicationContext,
            getString(R.string.ad_detail_pop_Key),
            resources.getInteger(R.integer.ad_detail_pop_Id)
        )

        //Tab設定
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //Select時
                frag = tab.position
                when (frag) {
                    0 -> {
                        // menu0を表示
                        menu0?.isVisible = true
                        // menu1を非表示
                        menu1?.isVisible = false
                        menu2?.isVisible = false
                    }
                    1 -> {
                        // menu0を非表示
                        menu0?.isVisible = false
                        // menu1を表示
                        menu1?.isVisible = true
                        menu2?.isVisible = true
                    }
                    else -> {
                        // menu0を非表示
                        menu0?.isVisible = false
                        // menu1を表示
                        menu1?.isVisible = false
                        menu2?.isVisible = false
                    }
                }
                if (Random().nextInt(100) >= 75) {
                    Log.d("Movie", "Play")
                    if (nendAdInterstitialVideo.isLoaded) {
                        nendAdInterstitialVideo.showAd(this@MainTabActivity)
                    }
                }
            }


            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        //Tab位置
        tabPosition = intent.getIntExtra("Position", 0)
        tabLayout.getTabAt(tabPosition)?.select()

        //通知
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        setNotification()

        viewReview()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu_tab, menu)

        // メニューアイテムを取得
        menu0 = menu.findItem(R.id.mainMenu1) as MenuItem
        menu1 = menu.findItem(R.id.mainMenu2) as MenuItem
        menu2 = menu.findItem(R.id.mainMenu3) as MenuItem
        when (tabPosition) {
            0 -> {
                // menu0を表示
                menu0?.isVisible = true
                // menu1を非表示
                menu1?.isVisible = false
                menu2?.isVisible = false
            }
            1 -> {
                // menu0を非表示
                menu0?.isVisible = false
                // menu1を表示
                menu1?.isVisible = true
                menu2?.isVisible = true
            }
            else -> {
                // menu0を非表示
                menu0?.isVisible = false
                // menu1を表示
                menu1?.isVisible = false
                menu2?.isVisible = false
            }
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mainMenu1 -> {
                startActivity(Intent(this, DazeInputActivity::class.java))
            }
            R.id.mainMenu2 ->{
                startActivity(Intent(this, MemoInputActivity::class.java))
            }
            R.id.mainMenu3 ->{
                //TODOここで全消しダイアログ

            }
            else -> error("対象外エラー１")
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, code: Intent?) {
        super.onActivityResult(requestCode, resultCode, code)
        NendAdInterstitial.showAd(this)
        //更新
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }


    private fun setNotification() {
        //呼び出す日時を設定する
        val triggerTime = Calendar.getInstance()
        triggerTime.add(Calendar.DATE, 7)

        //設定した日時で発行するIntentを生成
        val intent = Intent(this@MainTabActivity, Notice::class.java)
        val sender = PendingIntent.getBroadcast(this@MainTabActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //日時と発行するIntentをAlarmManagerにセットします
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.set(AlarmManager.RTC_WAKEUP, triggerTime.timeInMillis, sender)
    }

    private fun viewReview() {
        RateOrFeedback(this)
            // レビュー用ストアURL
            .setPlayStoreUrl(getString(R.string.reviewUrl))
            // 改善点・要望の送信先メールアドレス
            .setFeedbackEmail(getString(R.string.reviewMail))
            // 一度、評価するか改善点を送信するを選択した場合、それ以降はダイアログが表示されません。
            // この値をインクリメントすることで再度ダイアログが表示されるようになります。
            .setReviewRequestId(0)
            // 前回ダイアログを表示してから次にダイアログを表示してよいタイミングまでの期間です。
            .setIntervalFromPreviousShowing(60 * 60 * 3)//3時間
            // アプリをインストールしてから、ここで指定された期間はダイアログを表示しません。
            .setNotShowTermSecondsFromInstall(60 * 60)//1時間
            .showIfNeeds()
    }
}
