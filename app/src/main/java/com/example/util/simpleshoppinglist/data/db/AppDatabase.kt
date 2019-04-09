package com.example.util.simpleshoppinglist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.util.simpleshoppinglist.data.db.AppDatabase.Companion.DATABASE_VERSION
import com.example.util.simpleshoppinglist.data.model.Item

/**
 * Room database.
 */
@Database(entities = [Item::class], version = DATABASE_VERSION)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {

        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "appDatabase"

        // Database instance.
        private var INSTANCE: AppDatabase? = null

        private val lock = Any()

        // Singleton instantiation.
        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                        .databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}