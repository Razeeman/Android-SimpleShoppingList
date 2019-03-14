package com.example.util.simpleshoppinglist.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.util.AppExecutors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

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
            saveItem(ListItem(name = "item 1", color = 0xFF2196f3.toInt()))
            saveItem(ListItem(name = "item 2", color = 0xFF8e24aa.toInt()))
            saveItem(ListItem(name = "item 3", color = 0xFFf50057.toInt()))
            saveItem(ListItem(name = "item 4", color = 0xFF00c853.toInt()))
            saveItem(ListItem(name = "item 5", color = 0xFFffb300.toInt()))
        }
    }
}
