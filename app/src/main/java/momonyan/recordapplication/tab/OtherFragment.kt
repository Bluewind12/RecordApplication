package momonyan.recordapplication.tab

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.tab_tab3_other_layout.view.*
import momonyan.recordapplication.R

class OtherFragment : Fragment() {

    private lateinit var viewLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLayout = inflater.inflate(R.layout.tab_tab3_other_layout, container, false)


        //プライバシーポリシー
        viewLayout.homePageButton.setOnClickListener {
            val uri = Uri.parse(getString(R.string.privacyPolicyURL))
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
        //HP
        viewLayout.homePageButton.setOnClickListener {
            val uri = Uri.parse(getString(R.string.fantasyCherryBlossomURL))
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
        //他アプリ
        viewLayout.otherAppsButton.setOnClickListener {
            val uri = Uri.parse(getString(R.string.appsPageURL))
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        return viewLayout
    }
}