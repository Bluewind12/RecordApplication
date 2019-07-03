package momonyan.recordapplication

import android.app.Application

class AdapterData : Application() {
    private lateinit var adapter: TabAdapter

    fun setAdapter(ad: TabAdapter) {
        adapter = ad
    }

    fun getAdapter(): TabAdapter {
        return adapter
    }

}