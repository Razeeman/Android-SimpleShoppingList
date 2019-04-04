package com.example.util.simpleshoppinglist.ui.custom

import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

/**
 * Utility class to create a [Snackbar] that is showed above its anchor.
 */
class CustomSnackBar {

    companion object {

        fun make(anchor: View, message: String): Snackbar {
            return Snackbar.make(anchor, message, Snackbar.LENGTH_LONG).apply {
                (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
                    anchorId = anchor.id
                    anchorGravity = Gravity.TOP
                    gravity = Gravity.TOP
                }
            }
        }
    }
}