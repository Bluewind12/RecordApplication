package momonyan.recordapplication

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.tab_main_layout.*
import momonyan.recordapplication.database.AppDataBase

class MainTabActivity : AppCompatActivity() {
    private lateinit var mSectionsPagerAdapter: TabAdapter
    lateinit var db: AppDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "TestDataBase.db").build()


        setContentView(R.layout.tab_main_layout)
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mainMenu1 -> {
                startActivity(Intent(this, InputActivity::class.java))
            }
            else -> error("対象外エラー１")
        }
        return true
    }
}