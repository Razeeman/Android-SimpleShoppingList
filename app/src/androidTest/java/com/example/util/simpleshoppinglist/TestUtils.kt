package com.example.util.simpleshoppinglist

import android.preference.PreferenceManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.util.InstantExecutors

object TestUtils {

    fun clearDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = AppDatabase.getInstance(context)
        val repository = ItemsRepository.getInstance(InstantExecutors(), database.itemDao())
        repository.deleteAllItems()
    }

    fun clearPreferences() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()
    }

    fun openAddScreen() {
        onView(withId(R.id.fab_add)).perform(click())
    }

    fun openSettingsMenu() {
        onView(withId(R.id.menu_settings_group)).perform(click())
    }

    fun openArrangeItemsMenu() {
        onView(withId(R.id.menu_sort_group)).perform(click())
    }

    fun openSortMenu() {
        onView(withId(R.id.menu_sort_group)).perform(click())
        onView(ViewMatchers.withText(R.string.menu_sort)).perform(click())
    }

}