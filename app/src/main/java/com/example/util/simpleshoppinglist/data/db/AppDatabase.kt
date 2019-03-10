package com.example.util.simpleshoppinglist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.util.simpleshoppinglist.data.db.AppDatabase.Companion.DATABASE_VERSION
import com.example.util.simpleshoppinglist.data.model.ListItem

/**
 * Room database.
 */
@Database(entities = [ListItem::class], version = DATABASE_VERSION)
abstract class AppDatabase: RoomDatabase() {

    abstract fun listItemDao(): ListItemDao

    companion object {

        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "appDatabase"

        private var INSTANCE: AppDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                        .databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
                        .allowMainThreadQueries() // TODO only for testing.
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}