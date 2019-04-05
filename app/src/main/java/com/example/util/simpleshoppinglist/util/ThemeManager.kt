package com.example.util.simpleshoppinglist.util

import android.app.Activity
import android.content.Intent
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.prefs.AppThemeType

object ThemeManager {

    fun changeTheme(activity: Activity) {
        activity.finish()
        activity.startActivity(Intent(activity, activity::class.java))
        activity.overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }

    fun setTheme(activity: Activity, appTheme: AppThemeType) {
        when (appTheme) {
            AppThemeType.THEME_LIGHT -> activity.setTheme(R.style.AppTheme)
            AppThemeType.THEME_DARK -> activity.setTheme(R.style.AppThemeDark)
        }
    }
}