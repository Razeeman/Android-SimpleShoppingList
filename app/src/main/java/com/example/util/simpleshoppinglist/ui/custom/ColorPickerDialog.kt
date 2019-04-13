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

    // Listener to pass events to parent fragment.
    private var colorChangeListener: OnColorChangeListener? = null

    // Listener to set onto child fragment.
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

    companion object {

        private const val SELECTED_COLOR_BUNDLE_KEY = "selected_color"

        /**
         * Instantiation of a new dialog.
         *
         * @param selectedColor Color that is selected in this dialog.
         */
        fun newInstance(selectedColor: Int): ColorPickerDialog {
            return ColorPickerDialog().apply { init(selectedColor) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedColor = savedInstanceState?.getInt(SELECTED_COLOR_BUNDLE_KEY) ?: selectedColor
    }

    // Lint suppressed because dialog doesn't have a view before inflating.
    @Suppress("InflateParams")
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_COLOR_BUNDLE_KEY, selectedColor)
    }

    fun setOnColorChangeListener(listener: OnColorChangeListener) {
        colorChangeListener = listener
    }

    /**
     * Initialize dialog with parameters.
     */
    private fun init(selectedColor: Int) {
        this.selectedColor = selectedColor
    }
}