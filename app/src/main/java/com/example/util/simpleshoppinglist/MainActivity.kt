package com.example.util.simpleshoppinglist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.ui.main.MainFragment
import com.example.util.simpleshoppinglist.ui.main.MainPresenter
import com.example.util.simpleshoppinglist.util.AppExecutors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainFragment = supportFragmentManager.findFragmentById(R.id.content_frame) as MainFragment?
            ?: MainFragment.newInstance().also {
                supportFragmentManager.beginTransaction().add(R.id.content_frame, it).commit()
            }

        val database = AppDatabase.getInstance(this)
        val itemsRepository = ItemsRepository.getInstance(AppExecutors(), database.listItemDao())
        val mainPresenter = MainPresenter(itemsRepository)
        mainFragment.presenter = mainPresenter
    }
}
