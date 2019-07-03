package momonyan.recordapplication

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import momonyan.recordapplication.tab.MemoFragment
import momonyan.recordapplication.tab.OtherFragment
import momonyan.recordapplication.tab.OutputFragment

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val outputFragment = OutputFragment()
    private val memoFragment = MemoFragment()
    private val otherFragment = OtherFragment()

    override fun getItem(position: Int): Fragment {
        val setFragment: Fragment
        when (position) {
            0 -> setFragment = outputFragment
            1 -> setFragment = memoFragment
            2 -> setFragment = otherFragment
            else -> error("TabError")
        }
        return setFragment
    }

    fun reloadDaze() {
        outputFragment.reloadDataBase()
    }
    fun reloadMemo() {
        memoFragment.reloadDataBase()
    }

    override fun getCount(): Int {
        return 3
    }
}