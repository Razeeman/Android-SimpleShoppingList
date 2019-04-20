package com.example.util.simpleshoppinglist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.util.simpleshoppinglist.data.model.Item
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
class MainScreenTest {

    companion object {

        private const val OTHER_ITEM_NAME = "other item name"
        private const val ITEM1_NAME = "item 1"
        private const val ITEM2_NAME = "item 2"
        private const val ITEM3_NAME = "item 3"
        private const val ITEM4_NAME = "item 4"

        private val item1 = Item(name = ITEM1_NAME, color = 0xFF000000.toInt())
        private val item2 = Item(name = ITEM2_NAME, color = 0xFFFF0000.toInt())
        private val item3 = Item(name = ITEM3_NAME, color = 0xFF00FF00.toInt())
        private val item4 = Item(name = ITEM4_NAME, color = 0xFF0000FF.toInt())

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            AppUtil.clearPreferences()
            AppUtil.clearDatabase()
            AppUtil.saveItem(item1)
            AppUtil.saveItem(item2)
            AppUtil.saveItem(item3)
            AppUtil.saveItem(item4)
        }
    }

    @Rule @JvmField
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        listItems()
    }

    @Test
    fun listedItems() {
        onView(withText(ITEM1_NAME)).check(matches(isDisplayed()))
        onView(withText(ITEM2_NAME)).check(matches(isDisplayed()))
        onView(withText(ITEM3_NAME)).check(matches(isDisplayed()))
        onView(withText(ITEM4_NAME)).check(matches(isDisplayed()))
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

        NavUtils.updateItem(OTHER_ITEM_NAME, ITEM1_NAME)
    }

    @Test
    fun clearList() {
        NavUtils.clearList()

        onView(withText(R.string.no_items_in_the_list)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_items)).check(matches(not(isDisplayed())))
        onView(withText(ITEM1_NAME)).check(matches(not(isDisplayed())))
        onView(withText(ITEM2_NAME)).check(matches(not(isDisplayed())))
        onView(withText(ITEM3_NAME)).check(matches(not(isDisplayed())))
        onView(withText(ITEM4_NAME)).check(matches(not(isDisplayed())))
    }

    private fun listItems() {
        NavUtils.clearList()

        NavUtils.openAddScreen()
        onView(withText(ITEM3_NAME)).perform(click())
        onView(withText(ITEM1_NAME)).perform(click())
        onView(withText(ITEM2_NAME)).perform(click())
        onView(withText(ITEM4_NAME)).perform(click())

        onView(withId(R.id.fab_done)).perform(click())
    }

}