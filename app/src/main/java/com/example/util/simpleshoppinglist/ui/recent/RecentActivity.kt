package com.example.util.simpleshoppinglist.ui.recent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.util.ThemeManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.recent_activity.*
import javax.inject.Inject

class RecentActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: RecentContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getRecentComponent().inject(this)
        ThemeManager.setTheme(this, presenter.appTheme)
        setContentView(R.layout.recent_activity)

        // Setting up the toolbar.
        val toolbar = findViewById<BottomAppBar>(R.id.bottom_bar)
        setSupportActionBar(toolbar)

        // Setting up fab.
        fab_done.setOnClickListener {
            finish()
        }

        // Invisible placeholder to shift menu in bottom bar to the start.
        val fab = FloatingActionButton(this)
        fab.layoutParams = CoordinatorLayout.LayoutParams(0, 0).apply {
            anchorId = bottom_bar.id
        }
        coordinator.addView(fab)

        supportFragmentManager.findFragmentById(R.id.recent_content_frame) as RecentFragment?
            ?: RecentFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.recent_content_frame, it).commit()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        App.releaseRecentComponent()
    }
}
