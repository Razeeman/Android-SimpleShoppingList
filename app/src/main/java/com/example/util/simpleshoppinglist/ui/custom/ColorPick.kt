package com.example.util.simpleshoppinglist.ui.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.util.simpleshoppinglist.R
import kotlinx.android.synthetic.main.color_pick.view.*

/**
 * Custom view to represent one color pick in color palette.
 */
class ColorPick(context: Context) : FrameLayout(context) {

    constructor(context: Context, color: Int, checked: Boolean, colorSelectListener: OnColorSelectListener)
            : this(context) {

        LayoutInflater.from(context).inflate(R.layout.color_pick, this)
        iv_color.background = ColorStateDrawable(context, R.drawable.color_pick_drawable, color)

        if (checked) {
            iv_check.visibility = View.VISIBLE
        }

        setOnClickListener {
            colorSelectListener.onColorSelected(color)
        }
    }

    /**
     * Interface to listen to click events on this view.
     */
    interface OnColorSelectListener {

        fun onColorSelected(color: Int)

    }

}