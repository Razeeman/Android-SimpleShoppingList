package com.example.util.simpleshoppinglist.ui.additem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.util.simpleshoppinglist.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.additem_activity.*

class AddItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.additem_activity)

        // Setting up the toolbar.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        // Setting up fab.
        fab_done.setOnClickListener { view ->
            Snackbar.make(view, "Adding some items!", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }

        supportFragmentManager.findFragmentById(R.id.content_frame) as AddItemFragment?
            ?: AddItemFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.content_frame, it).commit()
            }
    }
}
