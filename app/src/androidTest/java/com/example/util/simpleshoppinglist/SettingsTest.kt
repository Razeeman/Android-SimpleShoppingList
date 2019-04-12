package com.example.util.simpleshoppinglist

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.util.simpleshoppinglist.testutil.AppUtil
import com.example.util.simpleshoppinglist.testutil.NavUtils
import com.example.util.simpleshoppinglist.ui.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsTest {

    @Rule @JvmField
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        AppUtil.clearPreferences()
    }

    @Test
    fun settings_nightMode() {
        NavUtils.switchNightMode()

        NavUtils.switchNightMode()
    }

}