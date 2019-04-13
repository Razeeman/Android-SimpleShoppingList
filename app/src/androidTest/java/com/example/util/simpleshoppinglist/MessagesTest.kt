package com.example.util.simpleshoppinglist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.util.simpleshoppinglist.testutil.NavUtils
import com.example.util.simpleshoppinglist.ui.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MessagesTest {

    companion object {

        private const val ITEM_NAME = "item"
        private const val OTHER_ITEM_NAME = "other item"

    }

    @Rule @JvmField
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Test
    fun noItemsInTheListMessage() {
        NavUtils.openAddScreen()
        NavUtils.addItem(ITEM_NAME)
        pressBack()
        NavUtils.clearList()

        onView(withText(R.string.no_items_in_the_list)).check(matches(isDisplayed()))
    }

    @Test
    fun noItemsInTheApp() {
        NavUtils.openAddScreen()
        NavUtils.deleteAll()

        onView(withText(R.string.no_items_added)).check(matches(isDisplayed()))

        pressBack()

        onView(withText(R.string.no_items_added)).check(matches(isDisplayed()))
    }

    @Test
    fun allItemsListed() {
        NavUtils.openAddScreen()
        NavUtils.deleteAll()
        NavUtils.addItem(ITEM_NAME)

        onView(withText(R.string.all_items_added)).check(matches(isDisplayed()))
    }

    @Test
    fun clearListMessage() {
        NavUtils.clearList()

        onView(withText(R.string.main_list_cleared)).check(matches(isDisplayed()))
    }

    @Test
    fun deleteAllMessage() {
        NavUtils.openAddScreen()
        NavUtils.deleteAll()

        onView(withText(R.string.recent_items_deleted)).check(matches(isDisplayed()))
    }

    @Test
    fun itemAdded() {
        NavUtils.openAddScreen()
        NavUtils.deleteAll()
        NavUtils.addItem(ITEM_NAME)

        onView(withText(R.string.recent_item_saved_message)).check(matches(isDisplayed()))
    }

    @Test
    fun itemUpdated() {
        NavUtils.openAddScreen()
        NavUtils.deleteAll()
        NavUtils.addItem(ITEM_NAME)
        pressBack()

        NavUtils.updateItem(ITEM_NAME, OTHER_ITEM_NAME)

        onView(withText(R.string.recent_item_updated_message)).check(matches(isDisplayed()))
    }

    @Test
    fun incorrectName() {
        NavUtils.openAddScreen()
        NavUtils.deleteAll()
        NavUtils.addItem(ITEM_NAME)
        pressBack()

        NavUtils.updateItem(ITEM_NAME, "")

        onView(withText(R.string.recent_incorrect_name)).check(matches(isDisplayed()))
    }

    @Test
    fun alreadyExist() {
        NavUtils.openAddScreen()
        NavUtils.deleteAll()
        NavUtils.addItem(ITEM_NAME)
        NavUtils.addItem(OTHER_ITEM_NAME)
        pressBack()

        NavUtils.updateItem(ITEM_NAME, OTHER_ITEM_NAME)

        onView(withText(R.string.recent_item_already_exists)).check(matches(isDisplayed()))
    }

}