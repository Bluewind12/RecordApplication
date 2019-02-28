package momonyan.recordapplication

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.tab_main_layout.*

class MainTabActivity : AppCompatActivity() {
    private lateinit var mSectionsPagerAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_main_layout)
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }
}