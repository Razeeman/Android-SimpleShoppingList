package com.example.util.simpleshoppinglist.testutil

import android.preference.PreferenceManager
import androidx.test.platform.app.InstrumentationRegistry
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.util.AppExecutors
import android.content.pm.ActivityInfo
import android.app.Activity
import android.content.res.Configuration
import androidx.test.ext.junit.rules.ActivityScenarioRule



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

    fun <T : Activity> changeOrientation(activityScenarioRule: ActivityScenarioRule<T>) {
        activityScenarioRule.scenario.onActivity { changeOrientation(it) }

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    }

    private fun changeOrientation(activity: Activity) {
        val currentOrientation = activity.resources.configuration.orientation
        val newOrientation = if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        activity.requestedOrientation = newOrientation
    }

}