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

        private val item1 = Item(name = "item 1", color = 0xFF000000.toInt())
        private val item2 = Item(name = "item 2", color = 0xFFFF0000.toInt())
        private val item3 = Item(name = "item 3", color = 0xFF00FF00.toInt())
        private val item4 = Item(name = "item 4", color = 0xFF0000FF.toInt())

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
        onView(withText(item1.name)).check(matches(isDisplayed()))
        onView(withText(item2.name)).check(matches(isDisplayed()))
        onView(withText(item3.name)).check(matches(isDisplayed()))
        onView(withText(item4.name)).check(matches(isDisplayed()))
    }

    @Test
    fun removeOneItem() {
        onView(withText(item1.name)).perform(swipeRight())

        onView(withText(item1.name)).check(doesNotExist())
        onView(withText(item2.name)).check(matches(isDisplayed()))
    }

    @Test
    fun updateItem() {
        NavUtils.updateItem(item1.name, OTHER_ITEM_NAME)

        NavUtils.updateItem(OTHER_ITEM_NAME, item1.name)
    }

    @Test
    fun clearList() {
        NavUtils.clearList()

        onView(withText(R.string.no_items_in_the_list)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_items)).check(matches(not(isDisplayed())))
        onView(withText(item1.name)).check(matches(not(isDisplayed())))
        onView(withText(item2.name)).check(matches(not(isDisplayed())))
        onView(withText(item3.name)).check(matches(not(isDisplayed())))
        onView(withText(item4.name)).check(matches(not(isDisplayed())))
    }

    private fun listItems() {
        NavUtils.clearList()

        NavUtils.openAddScreen()
        onView(withText(item3.name)).perform(click())
        onView(withText(item1.name)).perform(click())
        onView(withText(item2.name)).perform(click())
        onView(withText(item4.name)).perform(click())

        onView(withId(R.id.fab_done)).perform(click())
    }

}