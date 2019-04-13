package com.example.util.simpleshoppinglist

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.testutil.AppUtil
import com.example.util.simpleshoppinglist.testutil.NavUtils
import com.example.util.simpleshoppinglist.ui.main.MainActivity
import org.junit.Assert.fail
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    companion object {

        private const val ITEM_NAME = "test item"

        @BeforeClass @JvmStatic
        fun beforeClass() {
            AppUtil.clearDatabase()
            AppUtil.clearPreferences()
            ItemsRepository.clearInstance()
        }
    }

    @Rule @JvmField
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Test
    fun fromMainScreenToAddScreen() {
        onView(withId(R.id.main_content_frame)).check(matches(isDisplayed()))

        NavUtils.openAddScreen()

        onView(withId(R.id.recent_content_frame)).check(matches(isDisplayed()))
    }

    @Test
    fun fromAddScreenToMainScreen() {
        NavUtils.openAddScreen()

        onView(withId(R.id.recent_content_frame)).check(matches(isDisplayed()))

        onView(withId(R.id.fab_done)).perform(click())

        onView(withId(R.id.main_content_frame)).check(matches(isDisplayed()))
    }

    @Test
    fun pressBackFromAddScreen_returnsToMain() {
        NavUtils.openAddScreen()

        onView(withId(R.id.recent_content_frame)).check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.main_content_frame)).check(matches(isDisplayed()))
    }

    @Test
    fun settingsMenu() {
        NavUtils.openSettingsMenu()

        onView(withText(R.string.menu_night_mode)).check(matches(isDisplayed()))
    }

    @Test
    fun arrangeItemsMenu() {
        NavUtils.openArrangeItemsMenu()

        onView(withText(R.string.menu_sort)).check(matches(isDisplayed()))
    }

    @Test
    fun sortMenu() {
        NavUtils.openSortMenu()

        onView(withText(R.string.menu_sort_by_name)).check(matches(isDisplayed()))
    }

    @Test
    fun clearListDialog() {
        onView(withId(R.id.fab_clear)).perform(click())

        onView(withText(R.string.clear_list_dialog_message)).check(matches(isDisplayed()))
    }

    @Test
    fun pressBackOnClearListDialog_closesDialog() {
        onView(withId(R.id.fab_clear)).perform(click())

        pressBack()

        onView(withText(R.string.clear_list_dialog_message)).check(doesNotExist())
    }

    @Test
    fun deleteAllDialog() {
        NavUtils.openAddScreen()
        onView(withId(R.id.menu_delete_all)).perform(click())

        onView(withText(R.string.delete_all_dialog_message)).check(matches(isDisplayed()))
    }

    @Test
    fun pressBackOnDeleteAllDialog_closesDialog() {
        NavUtils.openAddScreen()
        onView(withId(R.id.menu_delete_all)).perform(click())

        pressBack()

        onView(withText(R.string.delete_all_dialog_message)).check(doesNotExist())
    }

    @Test
    fun addItemDialog() {
        NavUtils.openAddScreen()

        onView(withText(R.string.add_new_item)).perform(click())

        onView(withText(R.string.additem_dialog_color_label)).check(matches(isDisplayed()))
    }

    @Test
    fun pressBackFromAddItemDialog_closesDialog() {
        NavUtils.openAddScreen()

        onView(withText(R.string.add_new_item)).perform(click())

        pressBack()
        pressBack()

        onView(withText(R.string.additem_dialog_color_label)).check(doesNotExist())
    }

    @Test
    fun colorDialog() {
        NavUtils.openAddScreen()
        onView(withText(R.string.add_new_item)).perform(click())

        onView(withId(R.id.iv_item_color)).perform(click())

        onView(withText(R.string.color_picker_dialog_title)).check(matches(isDisplayed()))
    }

    @Test
    fun pressBackFromColorDialog_closesDialog() {
        NavUtils.openAddScreen()
        onView(withText(R.string.add_new_item)).perform(click())
        onView(withId(R.id.iv_item_color)).perform(click())

        pressBack()

        onView(withText(R.string.color_picker_dialog_title)).check(doesNotExist())
    }

    @Test
    fun editItemDialog() {
        NavUtils.openAddScreen()
        NavUtils.addItem(ITEM_NAME)
        pressBack()

        onView(withText(ITEM_NAME)).perform(longClick())

        onView(withText(R.string.edit_item_dialog_title)).check(matches(isDisplayed()))
    }

    @Test
    fun pressBackFromMainScreen_exitsApp() {
        assertExitApp()
    }

    @Test
    fun pressBackFromMainAfterAddScreen_exitsApp() {
        NavUtils.openAddScreen()
        onView(withId(R.id.fab_done)).perform(click())

        assertExitApp()
    }

    private fun assertExitApp() {
        try {
            pressBack()
            fail("App should be exited")
        } catch (e: NoActivityResumedException) {
            // Test passed.
        }
    }

}