package com.example.util.simpleshoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.ListItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppDatabase.getInstance(this).listItemDao().insert(ListItem())
    }
}
