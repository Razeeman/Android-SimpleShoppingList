package com.example.util.simpleshoppinglist.ui.custom

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment

class ColorPickerDialog: AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
            .setTitle("Choose item color")
            .create()
    }
}