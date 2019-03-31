package com.example.util.simpleshoppinglist.ui.recent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.recent_activity.*

class RecentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recent_activity)
        App.getRecentComponent()

        // Setting up the toolbar.
        val toolbar = findViewById<BottomAppBar>(R.id.bottom_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
