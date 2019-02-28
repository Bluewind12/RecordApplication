package momonyan.recordapplication

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import momonyan.recordapplication.tab.Tab1_OutputFragment
import momonyan.recordapplication.tab.Tab2_MemoFragment
import momonyan.recordapplication.tab.Tab3_InputFragment

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val setFragment: Fragment
        when (position) {
            0 -> setFragment = Tab1_OutputFragment()
            1 -> setFragment = Tab2_MemoFragment()
            2 -> setFragment = Tab3_InputFragment()
            else -> error("TabError")
        }
        return setFragment
    }

    override fun getCount(): Int {
        return 3
    }
}