package com.example.util.simpleshoppinglist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.util.simpleshoppinglist.ui.main.MainActivity
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsTest {

    companion object {
        @BeforeClass @JvmStatic
        fun beforeClass() {
            TestUtils.clearPreferences()
        }
    }

    @Rule @JvmField
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Test
    fun fromMainScreenToAddScreen() {
        TestUtils.openSettingsMenu()

        onView(withText(R.string.menu_night_mode)).perform(click())

        TestUtils.openSettingsMenu()

        onView(withText(R.string.menu_night_mode)).perform(click())
    }

}