package com.example.util.simpleshoppinglist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.util.simpleshoppinglist.testutil.AppUtil
import com.example.util.simpleshoppinglist.testutil.NavUtils
import com.example.util.simpleshoppinglist.ui.main.MainActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecentScreenTest {

    companion object {

        private const val OTHER_ITEM_NAME = "other item name"
        private const val ITEM1_NAME = "item 1"
        private const val ITEM2_NAME = "item 2"
        private const val ITEM3_NAME = "item 3"

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            AppUtil.clearPreferences()
            AppUtil.clearDatabase()
        }
    }

    @Rule @JvmField
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        addItems()
    }

    @Test
    fun addedItems() {
        onView(withText(ITEM1_NAME)).check(matches(isDisplayed()))
        onView(withText(ITEM2_NAME)).check(matches(isDisplayed()))
        onView(withText(ITEM3_NAME)).check(matches(isDisplayed()))
    }

    @Test
    fun removeOneItem() {
        onView(withText(ITEM1_NAME)).perform(swipeRight())

        onView(withText(ITEM1_NAME)).check(doesNotExist())
        onView(withText(ITEM2_NAME)).check(matches(isDisplayed()))
    }

    @Test
    fun updateItem() {
        NavUtils.updateItem(ITEM1_NAME, OTHER_ITEM_NAME)

        onView(withText(OTHER_ITEM_NAME)).check(matches(isDisplayed()))
    }

    @Test
    fun deleteAll() {
        NavUtils.deleteAll()

        onView(withText(R.string.no_items_added)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_items)).check(matches(not(isDisplayed())))
        onView(withText(ITEM1_NAME)).check(matches(not(isDisplayed())))
        onView(withText(ITEM2_NAME)).check(matches(not(isDisplayed())))
        onView(withText(ITEM3_NAME)).check(matches(not(isDisplayed())))
    }

    private fun addItems() {
        NavUtils.openAddScreen()
        NavUtils.deleteAll()

        NavUtils.addItem(ITEM1_NAME)
        NavUtils.addItem(ITEM2_NAME)
        NavUtils.addItem(ITEM3_NAME)

        onView(withId(R.id.fab_done)).perform(click())
        NavUtils.clearList()
        NavUtils.openAddScreen()
    }

}