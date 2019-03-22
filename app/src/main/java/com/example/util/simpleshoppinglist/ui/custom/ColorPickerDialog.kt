package com.example.util.simpleshoppinglist.ui.custom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.util.simpleshoppinglist.R
import kotlinx.android.synthetic.main.color_picker_dialog.view.*

class ColorPickerDialog: AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.color_picker_dialog, null)
        val palette = dialogView.color_palette

        // TODO test data.
        val colors = Array(14) { i -> when(i) {
            9 -> -12627531
            else -> 0xFF000000.toInt()
            }
        }

        palette.drawPalette(colors, 9)

        val dialog = AlertDialog.Builder(activity!!)
            .setView(dialogView)
            .setTitle("Choose item color")
            .create()

        return dialog
    }
}