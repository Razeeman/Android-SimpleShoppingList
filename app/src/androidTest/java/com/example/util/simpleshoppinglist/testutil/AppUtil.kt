package com.example.util.simpleshoppinglist.testutil

import android.preference.PreferenceManager
import androidx.test.platform.app.InstrumentationRegistry
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.util.AppExecutors

object AppUtil {

    fun saveItem(item: Item) {
        provideRepository().saveItem(item)
    }

    fun clearDatabase() {
        provideRepository().deleteAllItems()
    }

    fun clearPreferences() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()
    }

    private fun provideRepository(): BaseItemsRepository {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = AppDatabase.getInstance(context)
        return ItemsRepository.getInstance(AppExecutors(), database.itemDao())
    }

}