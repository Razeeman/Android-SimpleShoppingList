package com.example.util.simpleshoppinglist.ui.custom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.util.simpleshoppinglist.R
import kotlinx.android.synthetic.main.color_picker_dialog.view.*

class ColorPickerDialog: AppCompatDialogFragment() {

    private var selectedColor: Int = 0

    private var colorChangeListener: OnColorChangeListener? = null

    private val colorSelectedListener = object : ColorPick.OnColorSelectListener {
        override fun onColorSelected(color: Int) {
            colorChangeListener?.onColorChanged(color)
            dismiss()
        }
    }

    /**
     * Interface to listen to color change events.
     */
    interface OnColorChangeListener {

        fun onColorChanged(color: Int)

    }

    /**
     * Initialize dialog with parameters.
     */
    fun init(selectedColor: Int) {
        this.selectedColor = selectedColor
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.color_picker_dialog, null)
        val colors = resources.getIntArray(R.array.paletteColors)

        dialogView.color_palette.apply {
            setOnColorSelectListener(colorSelectedListener)
            drawPalette(colors, selectedColor)
        }

        return AlertDialog.Builder(activity!!)
            .setView(dialogView)
            .setTitle(getString(R.string.color_picker_dialog_title))
            .create()
    }

    fun setOnColorChangeListener(listener: OnColorChangeListener) {
        colorChangeListener = listener
    }
}