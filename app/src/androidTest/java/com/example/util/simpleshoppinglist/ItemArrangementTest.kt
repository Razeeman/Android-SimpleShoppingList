package com.example.util.simpleshoppinglist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyLeftOf
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.testutil.AppUtil
import com.example.util.simpleshoppinglist.testutil.NavigationUtils
import com.example.util.simpleshoppinglist.ui.main.MainActivity
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemArrangementTest {

    companion object {

        private val item1 = Item(name = "a", color = 0xFF000000.toInt())
        private val item2 = Item(name = "b", color = 0xFFFF0000.toInt())
        private val item3 = Item(name = "c", color = 0xFF000000.toInt())
        private val item4 = Item(name = "d", color = 0xFF0000FF.toInt())

        @BeforeClass @JvmStatic
        fun beforeClass() {
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
        AppUtil.clearPreferences()
        listItems()
    }

    @Test
    fun allItemsAreVisible() {
        onView(withText(item1.name)).check(matches(isDisplayed()))
        onView(withText(item2.name)).check(matches(isDisplayed()))
        onView(withText(item3.name)).check(matches(isDisplayed()))
        onView(withText(item4.name)).check(matches(isDisplayed()))
    }

    @Test
    fun hideChecked_clickOnItemHidesIt() {
        NavigationUtils.hideChecked()

        onView(withText(item3.name)).perform(click()).check(doesNotExist())
    }

    @Test
    fun hideChecked_clickOnAllItems_ClearsList() {
        NavigationUtils.hideChecked()

        onView(withText(item1.name)).perform(click())
        onView(withText(item2.name)).perform(click())
        onView(withText(item3.name)).perform(click())
        onView(withText(item4.name)).perform(click())

        onView(withText(R.string.no_items_in_the_list)).check(matches(isDisplayed()))
    }

    @Test
    fun itemsAreOrderedByTimeAdded() {
        onView(withText(item3.name)).check(isCompletelyLeftOf(withText(item1.name)))
        onView(withText(item1.name)).check(isCompletelyLeftOf(withText(item2.name)))
        onView(withText(item2.name)).check(isCompletelyLeftOf(withText(item4.name)))
    }

    @Test
    fun orderByName() {
        NavigationUtils.sortByName()

        onView(withText(item1.name)).check(isCompletelyLeftOf(withText(item2.name)))
        onView(withText(item2.name)).check(isCompletelyLeftOf(withText(item3.name)))
        onView(withText(item3.name)).check(isCompletelyLeftOf(withText(item4.name)))
    }

    @Test
    fun groupByColor() {
        NavigationUtils.sortByName()
        NavigationUtils.groupByColor()

        onView(withText(item1.name)).check(isCompletelyLeftOf(withText(item3.name)))
        onView(withText(item3.name)).check(isCompletelyLeftOf(withText(item2.name)))
        onView(withText(item2.name)).check(isCompletelyLeftOf(withText(item4.name)))
    }

    private fun listItems() {
        onView(withId(R.id.fab_clear)).perform(click())
        onView(withText(R.string.clear_list_dialog_positive)).perform(click())

        NavigationUtils.openAddScreen()
        onView(withText(item3.name)).perform(click())
        onView(withText(item1.name)).perform(click())
        onView(withText(item2.name)).perform(click())
        onView(withText(item4.name)).perform(click())

        pressBack()
    }

}