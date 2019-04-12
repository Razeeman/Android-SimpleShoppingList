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

    @Rule @JvmField
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Test
    fun noItemsInTheListMessage() {
        NavUtils.openAddScreen()
        NavUtils.addItem("Test item")
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
        NavUtils.addItem("Test item")

        onView(withText(R.string.all_items_added)).check(matches(isDisplayed()))
    }

}