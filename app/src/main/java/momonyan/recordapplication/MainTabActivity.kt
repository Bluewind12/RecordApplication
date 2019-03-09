package momonyan.recordapplication

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.tab_main_layout.*


class MainTabActivity : AppCompatActivity() {
    private lateinit var mSectionsPagerAdapter: TabAdapter
    private var frag: Int = 0
    private var tabPosition: Int = 0
    private var menu0: MenuItem? = null
    private var menu1: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tabPosition = intent.getIntExtra("Position", 0)
        setContentView(R.layout.tab_main_layout)
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
                    }
                    1 -> {
                        // menu0を非表示
                        menu0?.isVisible = false
                        // menu1を表示
                        menu1?.isVisible = true
                    }
                    else -> {
                        // menu0を非表示
                        menu0?.isVisible = false
                        // menu1を表示
                        menu1?.isVisible = false
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        tabLayout.getTabAt(tabPosition)?.select()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu_tab, menu)

        // メニューアイテムを取得
        menu0 = menu?.findItem(R.id.mainMenu1) as MenuItem
        menu1 = menu?.findItem(R.id.mainMenu2) as MenuItem
        when (tabPosition) {
            0 -> {
                // menu0を表示
                menu0?.isVisible = true
                // menu1を非表示
                menu1?.isVisible = false
            }
            1 -> {
                // menu0を非表示
                menu0?.isVisible = false
                // menu1を表示
                menu1?.isVisible = true
            }
            else -> {
                // menu0を非表示
                menu0?.isVisible = false
                // menu1を表示
                menu1?.isVisible = false
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
            else -> error("対象外エラー１")
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, code: Intent?) {
        super.onActivityResult(requestCode, resultCode, code)
        //更新
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }
}
