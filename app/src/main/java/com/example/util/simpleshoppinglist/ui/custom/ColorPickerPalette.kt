package com.example.util.simpleshoppinglist.ui.custom

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.widget.TableLayout
import android.widget.TableRow
import com.example.util.simpleshoppinglist.R
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat

/**
 * A custom color picker view that shows a grid of color to choose from.
 */
class ColorPickerPalette : TableLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    // Width of the palette.
    private var columns: Int = 5
    // Size of the one color pick.
    private var colorPickSize = resources.getDimensionPixelSize(R.dimen.color_pick_size)
    // Margin between color picks.
    private var colorPickMargin = resources.getDimensionPixelSize(R.dimen.color_pick_margin)

    /**
     * Adds color pick views in rows and adds rows to the main view.
     */
    fun drawPalette(colors: Array<Int>, selected: Int) {
        var row = createRow()

        for ((colorIndex, color) in colors.withIndex()) {
            val colorPick = ImageView(context).apply {
                layoutParams = TableRow.LayoutParams(colorPickSize, colorPickSize).apply {
                    setMargins(colorPickMargin, colorPickMargin, colorPickMargin, colorPickMargin)
                }
            }
            val drawable = ContextCompat.getDrawable(context.applicationContext, R.drawable.item_drawable)
            drawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
            colorPick.background = drawable

            if (colorIndex > 0 && colorIndex % columns == 0) {
                addView(row)
                row = createRow()
            }

            row.addView(colorPick)
        }

        addView(row)
    }

    /**
     * Creates view for table row.
     */
    private fun createRow(): TableRow {
        return TableRow(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }
}