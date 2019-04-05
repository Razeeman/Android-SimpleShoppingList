package com.example.util.simpleshoppinglist.data.prefs

/**
 * Preferences helper contract.
 */
interface BasePreferenceHelper {

    var appTheme: AppThemeType

    var hideChecked: Boolean

    var sortBy: ItemsSortType

}