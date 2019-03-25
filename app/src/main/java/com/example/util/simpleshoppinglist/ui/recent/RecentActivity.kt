package com.example.util.simpleshoppinglist.ui.recent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import kotlinx.android.synthetic.main.recent_activity.*

class RecentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recent_activity)
        App.getRecentComponent()

        // Setting up the toolbar.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        // Setting up fab.
        fab_done.setOnClickListener {
            finish()
        }

        supportFragmentManager.findFragmentById(R.id.content_frame) as RecentFragment?
            ?: RecentFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.content_frame, it).commit()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        App.releaseRecentComponent()
    }
}
