package com.example.util.simpleshoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.util.AppExecutors
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listItem = ListItem(name = "new item")
        val repository = ItemsRepository
            .getInstance(AppExecutors(), AppDatabase.getInstance(this).listItemDao())

        repository.saveListItem(listItem)
        repository.loadListItem(listItem.id, object: BaseItemsRepository.LoadItemCallback {
            override fun onItemLoaded(item: ListItem) {
                tv_hello.text = item.name
            }

            override fun onDataNotAvailable() {
                tv_hello.text = "error"
            }
        })
    }
}
