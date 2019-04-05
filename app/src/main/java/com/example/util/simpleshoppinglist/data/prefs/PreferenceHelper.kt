package com.example.util.simpleshoppinglist.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the preference helper.
 *
 * @param sharedPreferences SharedPreferences to access preferences.
 */
@Singleton
class PreferenceHelper
@Inject constructor(private val sharedPreferences: SharedPreferences)
    : BasePreferenceHelper {

    companion object {

        /**
         * Preferences keys and default values.
         */

        private const val PREFS_APP_THEME_KEY = "prefs_app_theme_key"
        private val PREFS_APP_THEME_DEFAULT = AppThemeType.values().indexOf(AppThemeType.THEME_LIGHT)

        private const val PREFS_HIDE_CHECKED_KEY = "prefs_hide_checked_key"
        private const val PREFS_HIDE_CHECKED_DEFAULT = false

        private const val PREFS_SORT_BY_KEY = "prefs_sort_by_key"
        private val PREFS_SORT_BY_DEFAULT = ItemsSortType.values().indexOf(ItemsSortType.DEFAULT)

    }

    /**
     * Preference to store app theme.
     * Preference stored as an Int index of [AppThemeType] enum type.
     */
    override var appTheme: AppThemeType
        get() = AppThemeType.values()[sharedPreferences.getInt(PREFS_APP_THEME_KEY, PREFS_APP_THEME_DEFAULT)]
        set(value) = sharedPreferences.edit {
            putInt(PREFS_APP_THEME_KEY, AppThemeType.values().indexOf(value))
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