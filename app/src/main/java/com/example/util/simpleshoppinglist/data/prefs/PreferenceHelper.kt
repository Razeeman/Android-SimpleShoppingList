package com.example.util.simpleshoppinglist.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * implementation of the preferences helper.
 *
 * @param sharedPreferences SharedPreferences to access preferences.
 */
class PreferenceHelper
(private val sharedPreferences: SharedPreferences)
    : BasePreferenceHelper {

    companion object {

        private const val PREFS_HIDE_CHECKED_KEY = "prefs_hide_checked_key"
        private const val PREFS_HIDE_CHECKED_DEFAULT = false

        private const val PREFS_SORT_BY_KEY = "prefs_sort_by_key"
        private val PREFS_SORT_BY_DEFAULT = ItemsSortType.values().indexOf(ItemsSortType.DEFAULT)

    }

    /**
     * Preference to decide if checked items on the main view should be hidden or not.
     */
    override var hideChecked: Boolean
        get() = sharedPreferences.getBoolean(PREFS_HIDE_CHECKED_KEY, PREFS_HIDE_CHECKED_DEFAULT)
        set(value) = sharedPreferences.edit {
            putBoolean(PREFS_HIDE_CHECKED_KEY, value)
        }

    /**
     * Preference to store items sort type.
     * Preference stored as an Int index of [ItemsSortType] enum type.
     */
    override var sortBy: ItemsSortType
        get() = ItemsSortType.values()[sharedPreferences.getInt(PREFS_SORT_BY_KEY, PREFS_SORT_BY_DEFAULT)]
        set(value) = sharedPreferences.edit {
            putInt(PREFS_SORT_BY_KEY, ItemsSortType.values().indexOf(value))
        }
}