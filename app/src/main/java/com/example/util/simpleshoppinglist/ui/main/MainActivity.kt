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

        val mainFragment = supportFragmentManager.findFragmentById(R.id.content_frame) as MainFragment?
            ?: MainFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.content_frame, it).commit()
            }

        val database = AppDatabase.getInstance(this)
        val itemsRepository = ItemsRepository.getInstance(AppExecutors(), database.listItemDao())

        // TODO test data
        itemsRepository.deleteAllItems()
        itemsRepository.apply {
            deleteAllItems()
            saveItem(ListItem(name = "item 1", color = 0x2196f3))
            saveItem(ListItem(name = "item 2", color = 0x8e24aa))
            saveItem(ListItem(name = "item 3", color = 0xf50057))
            saveItem(ListItem(name = "item 4", color = 0x00c853))
            saveItem(ListItem(name = "item 5", color = 0xffb300))
        }

        val mainPresenter = MainPresenter(itemsRepository)
        mainFragment.presenter = mainPresenter
    }
}
