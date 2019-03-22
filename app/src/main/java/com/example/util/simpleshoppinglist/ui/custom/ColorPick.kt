package com.example.util.simpleshoppinglist.ui.custom

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.util.simpleshoppinglist.R
import kotlinx.android.synthetic.main.color_pick.view.*

class ColorPick(context: Context, color: Int, checked: Boolean, colorSelectListener: OnColorSelectListener)
    : FrameLayout(context) {

    interface OnColorSelectListener {

        fun onColorSelected(color: Int)

    }

    init {
        LayoutInflater.from(context).inflate(R.layout.color_pick, this)
        iv_color.background = resources.getDrawable(R.drawable.color_pick_drawable).apply {
            colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        if (checked) {
            iv_check.visibility = View.VISIBLE
        }

        this.setOnClickListener {
            colorSelectListener.onColorSelected(color)
        }
    }

}