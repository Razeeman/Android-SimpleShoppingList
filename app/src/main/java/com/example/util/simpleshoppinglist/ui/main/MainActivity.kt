package com.example.util.simpleshoppinglist.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.ui.additem.AddItemActivity
import com.example.util.simpleshoppinglist.util.AppExecutors
import kotlinx.android.synthetic.main.main_activity.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.util.simpleshoppinglist.R.layout.main_activity)
        App.getAppComponent().inject(this)

        // Setting up the toolbar.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setting up fabs.
        fab_add.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
        fab_clear.setOnClickListener {
            presenter.clearList()
        }

        supportFragmentManager.findFragmentById(R.id.content_frame) as MainFragment?
            ?: MainFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.content_frame, it).commit()
            }

        // TODO test data
        val database = AppDatabase.getInstance(this)
        val itemsRepository = ItemsRepository.getInstance(AppExecutors(), database.listItemDao())
        itemsRepository.deleteAllItems()
        itemsRepository.apply {
            deleteAllItems()
            saveItem(ListItem(name = "item 1", color = 0xFF2196f3.toInt()).also { it.isActive = true })
            saveItem(ListItem(name = "item 2 with long name", color = 0xFF8e24aa.toInt()).also { it.isActive = true })
            saveItem(ListItem(name = "item 3", color = 0xFFf50057.toInt()))
            saveItem(ListItem(name = "item 4", color = 0xFF00c853.toInt()))
            saveItem(ListItem(name = "item 5 with very very long name", color = 0xFFffb300.toInt()))
        }
    }
}
