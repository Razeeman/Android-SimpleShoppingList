package com.example.util.simpleshoppinglist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.ui.main.MainActivity
import org.junit.Assert.fail
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    companion object {
        @BeforeClass @JvmStatic
        fun beforeClass() {
            TestUtils.clearDatabase()
            TestUtils.clearPreferences()
            ItemsRepository.clearInstance()
        }
    }

    @Rule @JvmField
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Test
    fun fromMainScreenToAddScreen() {
        onView(withId(R.id.main_content_frame)).check(matches(isDisplayed()))

        TestUtils.openAddScreen()

        onView(withId(R.id.recent_content_frame)).check(matches(isDisplayed()))
    }

    @Test
    fun fromAddScreenToMainScreen() {
        TestUtils.openAddScreen()

        onView(withId(R.id.recent_content_frame)).check(matches(isDisplayed()))

        onView(withId(R.id.fab_done)).perform(click())

        onView(withId(R.id.main_content_frame)).check(matches(isDisplayed()))
    }

    @Test
    fun pressBackFromAddScreen_returnsToMain() {
        TestUtils.openAddScreen()

        onView(withId(R.id.recent_content_frame)).check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.main_content_frame)).check(matches(isDisplayed()))
    }

    @Test
    fun settingsMenu() {
        TestUtils.openSettingsMenu()

        onView(withText(R.string.menu_night_mode)).check(matches(isDisplayed()))
    }

    @Test
    fun arrangeItemsMenu() {
        TestUtils.openArrangeItemsMenu()

        onView(withText(R.string.menu_sort)).check(matches(isDisplayed()))
    }

    @Test
    fun sortMenu() {
        TestUtils.openSortMenu()

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
        onView(withId(R.id.fab_add)).perform(click())
        onView(withId(R.id.menu_delete_all)).perform(click())

        onView(withText(R.string.delete_all_dialog_message)).check(matches(isDisplayed()))
    }

    @Test
    fun pressBackOnDeleteAllDialog_closesDialog() {
        onView(withId(R.id.fab_add)).perform(click())
        onView(withId(R.id.menu_delete_all)).perform(click())

        pressBack()

        onView(withText(R.string.delete_all_dialog_message)).check(doesNotExist())
    }

    @Test
    fun pressBackFromMainScreen_exitsApp() {
        assertExitApp()
    }

    @Test
    fun pressBackFromMainAfterAddScreen_exitsApp() {
        onView(withId(R.id.fab_add)).perform(click())
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