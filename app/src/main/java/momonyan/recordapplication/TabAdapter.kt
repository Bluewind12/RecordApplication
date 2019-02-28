package momonyan.recordapplication

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import momonyan.recordapplication.tab.InputFragment
import momonyan.recordapplication.tab.MemoFragment
import momonyan.recordapplication.tab.OutputFragment

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val setFragment: Fragment
        when (position) {
            0 -> setFragment = OutputFragment()
            1 -> setFragment = MemoFragment()
            2 -> setFragment = InputFragment()
            else -> error("TabError")
        }
        return setFragment
    }

    override fun getCount(): Int {
        return 3
    }
}