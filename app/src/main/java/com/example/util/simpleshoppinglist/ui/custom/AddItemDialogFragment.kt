package com.example.util.simpleshoppinglist.ui.custom

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment

// TODO DialogFragment instead?
class AddItemDialogFragment: AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!).apply {
            setTitle("Add new item")
            setPositiveButton("Yes") { dialog, which -> dismiss() }
            setNegativeButton("Cancel") { dialog, which -> dismiss() }
            //setView()
        }.create()
    }
}