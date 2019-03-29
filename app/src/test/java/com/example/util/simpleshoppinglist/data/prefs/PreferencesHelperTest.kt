package com.example.util.simpleshoppinglist.data.prefs

import android.content.SharedPreferences
import com.example.util.simpleshoppinglist.argumentCaptor
import com.example.util.simpleshoppinglist.capture
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Tests for getters and setters are probably unnecessary, created for practice purposes.
 */
class PreferenceHelperTest {

    companion object {

        private const val HIDE_CHECKED_TEST_VALUE = true
        private const val SORT_BY_TEST_VALUE = 1

    }

    @Mock private lateinit var sharedPreferences: SharedPreferences
    @Mock private lateinit var editor: SharedPreferences.Editor

    private lateinit var preferenceHelper: PreferenceHelper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        preferenceHelper = PreferenceHelper(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
    }

    @After
    fun tearDown() {
        // Not used.
    }

    @Test
    fun getHideChecked() {
        // When preference is accessed then its value is returned.
        `when`(sharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(HIDE_CHECKED_TEST_VALUE)
        assertThat(preferenceHelper.hideChecked, `is`(HIDE_CHECKED_TEST_VALUE))
    }

    @Test
    fun setHideChecked() {
        // When preference is set.
        preferenceHelper.hideChecked = HIDE_CHECKED_TEST_VALUE

        // Then SharedPreferences is called to put this new value in storage.
        val captorKey = argumentCaptor<String>()
        val captorValue = argumentCaptor<Boolean>()
        verify(editor).putBoolean(capture(captorKey), capture(captorValue))
        verify(editor).apply()
        assertThat(captorValue.value, `is`(HIDE_CHECKED_TEST_VALUE))
    }

    @Test
    fun getSortBy() {
        // When preference is accessed then its value is returned.
        `when`(sharedPreferences.getInt(anyString(), anyInt())).thenReturn(SORT_BY_TEST_VALUE)
        assertThat(preferenceHelper.sortBy, `is`(ItemsSortType.values()[SORT_BY_TEST_VALUE]))
    }

    @Test
    fun setSortBy() {
        // When preference is set.
        preferenceHelper.sortBy = ItemsSortType.values()[SORT_BY_TEST_VALUE]

        // Then SharedPreferences is called to put this new value in storage.
        val captorKey = argumentCaptor<String>()
        val captorValue = argumentCaptor<Int>()
        verify(editor).putInt(capture(captorKey), capture(captorValue))
        verify(editor).apply()
        assertThat(captorValue.value, `is`(SORT_BY_TEST_VALUE))
    }
}