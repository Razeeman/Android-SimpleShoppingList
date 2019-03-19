package com.example.util.simpleshoppinglist.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.ui.additem.AddItemActivity
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.util.simpleshoppinglist.R.layout.main_activity)
        App.getMainComponent().inject(this)

        // Setting up the toolbar.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setting up fabs.
        fab_add.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        fab_clear.setOnClickListener {
           AlertDialog.Builder(this)
               .setTitle("Warning")
               .setMessage("Do you want to clear the list?")
               .setNegativeButton("Cancel") { _, _ -> }
               .setPositiveButton("Clear") { _, _ ->
                   presenter.apply {
                       clearList()
                       loadData()
                   }
               }
               .create().show()
        }

        supportFragmentManager.findFragmentById(R.id.content_frame) as MainFragment?
            ?: MainFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.content_frame, it).commit()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        App.releaseMainComponent()
    }
}
