package com.example.util.simpleshoppinglist.data.prefs

/**
 * Preferences helper contract.
 */
interface BasePreferenceHelper {

    var appTheme: AppThemeType

    var groupByColor: Boolean

    var hideChecked: Boolean

    var sortBy: ItemsSortType

}