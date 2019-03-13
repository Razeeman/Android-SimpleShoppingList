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
        itemsRepository.apply {
            deleteAllItems()
            saveItem(ListItem(name = "some one item"))
            saveItem(ListItem(name = "some other item"))
        }

        val mainPresenter = MainPresenter(itemsRepository)
        mainFragment.presenter = mainPresenter
    }
}
