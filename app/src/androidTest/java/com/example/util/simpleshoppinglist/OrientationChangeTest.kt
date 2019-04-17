package com.example.util.simpleshoppinglist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.util.simpleshoppinglist.testutil.AppUtil
import com.example.util.simpleshoppinglist.testutil.NavUtils
import com.example.util.simpleshoppinglist.ui.main.MainActivity
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class OrientationChangeTest {

    companion object {

        private const val ITEM_NAME = "test item"
        private const val OTHER_ITEM_NAME = "other test item"
        private const val NEW_ITEM_NAME = "new test item"

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            AppUtil.clearPreferences()
            AppUtil.clearPreferences()
        }

    }

    @Rule @JvmField
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Before
    fun before() {
        NavUtils.openAddScreen()
        NavUtils.deleteAll()
        NavUtils.addItem(ITEM_NAME)
        NavUtils.addItem(OTHER_ITEM_NAME)
        onView(withId(R.id.fab_done)).perform(click())
    }

    @Test
    fun rotateMainScreen() {
        onView(withText(ITEM_NAME)).check(matches(isDisplayed()))
        onView(withText(OTHER_ITEM_NAME)).check(matches(isDisplayed()))

        AppUtil.changeOrientation(activityScenarioRule)

        onView(withText(ITEM_NAME)).check(matches(isDisplayed()))
        onView(withText(OTHER_ITEM_NAME)).check(matches(isDisplayed()))
    }

    @Test
    fun rotateInAddScreen() {
        NavUtils.clearList()
        NavUtils.openAddScreen()

        onView(withText(ITEM_NAME)).check(matches(isDisplayed()))
        onView(withText(OTHER_ITEM_NAME)).check(matches(isDisplayed()))

        AppUtil.changeOrientation(activityScenarioRule)

        onView(withText(ITEM_NAME)).check(matches(isDisplayed()))
        onView(withText(OTHER_ITEM_NAME)).check(matches(isDisplayed()))
    }

    @Test
    fun addItemDialogPersists() {
        NavUtils.openAddScreen()

        onView(withText(R.string.add_new_item)).perform(click())
        onView(withId(R.id.et_item_name)).perform(replaceText(NEW_ITEM_NAME))
        onView(withText(NEW_ITEM_NAME)).check(matches(isDisplayed()))

        AppUtil.changeOrientation(activityScenarioRule)

        onView(withText(NEW_ITEM_NAME)).check(matches(isDisplayed()))
    }

    @Test
    fun updateItemDialogPersists() {
        onView(withText(ITEM_NAME)).perform(longClick())
        onView(withId(R.id.et_item_name)).perform(replaceText(NEW_ITEM_NAME))
        onView(withText(NEW_ITEM_NAME)).check(matches(isDisplayed()))

        AppUtil.changeOrientation(activityScenarioRule)

        onView(withText(NEW_ITEM_NAME)).check(matches(isDisplayed()))
    }

    @Test
    fun colorDialogPersists() {
        onView(withText(ITEM_NAME)).perform(longClick())
        onView(withId(R.id.iv_item_color)).perform(click())
        onView(withText(R.string.color_picker_dialog_title)).check(matches(isDisplayed()))

        AppUtil.changeOrientation(activityScenarioRule)

        onView(withText(R.string.color_picker_dialog_title)).check(matches(isDisplayed()))
    }

}