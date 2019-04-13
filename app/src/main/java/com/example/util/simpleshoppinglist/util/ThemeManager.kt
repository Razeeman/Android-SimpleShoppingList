package com.example.util.simpleshoppinglist.util

import android.app.Activity
import android.content.Intent
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.prefs.AppThemeType

/**
 * Utility methods used for theme switching.
 */
object ThemeManager {

    /**
     * Restarts activity for it to be recreated with a new theme.
     *
     * @param activity Activity which needs to be recreated with a new theme.
     */
    fun changeTheme(activity: Activity) {
        activity.finish()
        activity.startActivity(Intent(activity, activity::class.java))
        activity.overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

    /**
     * Set new theme onto activity. Should be used in onCreate before setContentView.
     *
     * @param activity Activity to set new theme onto.
     * @param appTheme New theme to be set.
     */
    fun setTheme(activity: Activity, appTheme: AppThemeType) {
        when (appTheme) {
            AppThemeType.THEME_LIGHT -> activity.setTheme(R.style.AppTheme)
            AppThemeType.THEME_DARK -> activity.setTheme(R.style.AppThemeDark)
        }
    }
}