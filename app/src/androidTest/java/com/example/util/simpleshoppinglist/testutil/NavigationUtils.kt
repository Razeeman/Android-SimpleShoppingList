package com.example.util.simpleshoppinglist.testutil

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.util.simpleshoppinglist.R

object NavigationUtils {

    fun openAddScreen() {
        onView(withId(R.id.fab_add)).perform(click())
    }

    fun openSettingsMenu() {
        onView(withId(R.id.menu_settings_group)).perform(click())
    }

    fun switchNightMode() {
        openSettingsMenu()
        onView(withText(R.string.menu_night_mode)).perform(click())
    }

    fun openArrangeItemsMenu() {
        onView(withId(R.id.menu_sort_group)).perform(click())
    }

    fun hideChecked() {
        openArrangeItemsMenu()
        onView(withText(R.string.menu_hide_checked)).perform(click())
    }

    fun groupByColor() {
        openArrangeItemsMenu()
        onView(withText(R.string.menu_group_by_color)).perform(click())
    }

    fun openSortMenu() {
        onView(withId(R.id.menu_sort_group)).perform(click())
        onView(withText(R.string.menu_sort)).perform(click())
    }

    fun sortByName() {
        openSortMenu()
        onView(withText(R.string.menu_sort_by_name)).perform(click())
    }

}