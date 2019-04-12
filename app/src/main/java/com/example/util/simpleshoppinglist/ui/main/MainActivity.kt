package com.example.util.simpleshoppinglist.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.ui.recent.RecentActivity
import com.example.util.simpleshoppinglist.util.ThemeManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getMainComponent().inject(this)
        ThemeManager.setTheme(this, presenter.appTheme)
        setContentView(R.layout.main_activity)

        // Setting up the toolbar.
        val toolbar = findViewById<BottomAppBar>(R.id.bottom_bar)
        setSupportActionBar(toolbar)

        // Setting up fabs.
        fab_add.setOnClickListener {
            startActivity(Intent(this, RecentActivity::class.java))
        }

        fab_clear.setOnClickListener {
            showClearListDialog()
        }

        // Invisible placeholder to shift menu in bottom bar to the start.
        val fab = FloatingActionButton(this)
        fab.layoutParams = CoordinatorLayout.LayoutParams(0, 0).apply {
            anchorId = bottom_bar.id
        }
        coordinator.addView(fab)

        supportFragmentManager.findFragmentById(R.id.main_content_frame) as MainFragment?
            ?: MainFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.main_content_frame, it).commit()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        App.releaseMainComponent()
    }

    private fun showClearListDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.clear_list_dialog_title))
            .setMessage(getString(R.string.clear_list_dialog_message))
            .setNegativeButton(getString(R.string.clear_list_dialog_negative), null)
            .setPositiveButton(getString(R.string.clear_list_dialog_positive)) { _, _ ->
                presenter.clearList()
            }
            .create().show()
    }
}
