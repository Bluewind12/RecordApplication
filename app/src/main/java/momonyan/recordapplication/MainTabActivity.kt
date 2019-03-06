package momonyan.recordapplication

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.tab_main_layout.*


class MainTabActivity : AppCompatActivity() {
    private lateinit var mSectionsPagerAdapter: TabAdapter
    private var frag: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.tab_main_layout)
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //Select時
                frag = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

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
            else -> error("対象外エラー１")
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)

        Log.d("Debug", "$frag")
        // メニューアイテムを取得
        val menu0 = menu.findItem(R.id.mainMenu1) as MenuItem
        val menu1 = menu.findItem(R.id.mainMenu2) as MenuItem

        when (frag) {
            0 -> {
                // menu0を表示
                menu0.isVisible = true
                // menu1を非表示
                menu1.isVisible = false
            }
            1 -> {
                // menu0を非表示
                menu0.isVisible = false
                // menu1を表示
                menu1.isVisible = true
            }
            else -> {
                // menu0を非表示
                menu0.isVisible = false
                // menu1を表示
                menu1.isVisible = false
            }
        }
        return true
    }

}
