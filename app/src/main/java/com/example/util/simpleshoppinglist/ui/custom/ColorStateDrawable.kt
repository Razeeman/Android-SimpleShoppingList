package com.example.util.simpleshoppinglist.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.StateListDrawable
import androidx.core.content.ContextCompat

/**
 * Drawable that changes it color after being pressed.
 *
 * @param context    Context to access resources.
 * @param drawableId Drawable to put onto this view.
 * @param color      Color to put onto drawable.
 */
class ColorStateDrawable(context: Context, drawableId: Int, private val color: Int) : StateListDrawable() {

    companion object {

        private const val PRESSED_STATE_MULTIPLIER = 0.50f

    }

    init {
        val drawable = ContextCompat.getDrawable(context.applicationContext, drawableId)
        drawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        this.addState(IntArray(0), drawable)
    }

    override fun onStateChange(stateSet: IntArray?): Boolean {
        var stateChanged = false

        // Decide if the state was changed.
        if (stateSet != null) {
            for (state in stateSet) {
                if (state == android.R.attr.state_pressed || state == android.R.attr.state_focused) {
                    stateChanged = true
                }
            }
        }

        val newColor = if (stateChanged) {
            getPressedColor(color)
        } else {
            color
        }
        this.colorFilter = PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN)

        return super.onStateChange(stateSet)
    }

    /**
     * Change value of a given color.
     */
    private fun getPressedColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        // Change value of this color to make it dimmer.
        hsv[2] = hsv[2] * PRESSED_STATE_MULTIPLIER
        return Color.HSVToColor(hsv)
    }

}