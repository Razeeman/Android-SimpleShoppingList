package com.example.util.simpleshoppinglist.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import com.example.util.simpleshoppinglist.R

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

    // Listener to set onto child fragments and pass event to parent fragment.
    private lateinit var colorSelectListener: ColorPick.OnColorSelectListener

    /**
     * Adds color pick views in rows and adds rows to the main view.
     *
     * @param colors   Array of colors to show in the palette.
     * @param selected One color that is selected.
     */
    fun drawPalette(colors: IntArray, selected: Int) {
        var row = createRow()

        for ((colorIndex, color) in colors.withIndex()) {
            val colorPick = ColorPick(context, color, color == selected, colorSelectListener)
            val params = TableRow.LayoutParams(colorPickSize, colorPickSize).apply {
                setMargins(colorPickMargin, colorPickMargin, colorPickMargin, colorPickMargin)
            }
            colorPick.layoutParams = params

            if (colorIndex > 0 && colorIndex % columns == 0) {
                addView(row)
                row = createRow()
            }

            row.addView(colorPick)
        }

        addView(row)
    }

    fun setOnColorSelectListener(listener: ColorPick.OnColorSelectListener) {
        colorSelectListener = listener
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