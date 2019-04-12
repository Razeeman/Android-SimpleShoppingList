package com.example.util.simpleshoppinglist.testutil

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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

    fun clearList() {
        onView(withId(R.id.fab_clear)).perform(click())
        onView(withText(R.string.clear_list_dialog_positive)).perform(click())
    }

    fun deleteAll() {
        onView(withId(R.id.menu_delete_all)).perform(click())
        onView(withText(R.string.delete_all_dialog_positive)).perform(click())
    }

    fun addItem(name: String) {
        onView(withText(R.string.add_new_item)).perform(click())
        onView(withId(R.id.et_item_name)).perform(replaceText(name))
        onView(withText(R.string.additem_dialog_positive)).perform(click())
    }

}