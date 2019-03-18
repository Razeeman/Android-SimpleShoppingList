package com.example.util.simpleshoppinglist.ui.custom

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment

// TODO DialogFragment instead?
class AddItemDialogFragment: AppCompatDialogFragment() {

    private var listener: ButtonClickListener? = null

    interface ButtonClickListener {

        fun onPositiveButton()
        fun onNegativeButton()

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!).apply {
            setTitle("Add new item")
            setPositiveButton("Yes") { _, _ ->
                listener?.onPositiveButton()
            }
            setNegativeButton("Cancel") { _, _ ->
                listener?.onNegativeButton()
            }
            // TODO setView()
        }.create()
    }

    fun setListener(listener: ButtonClickListener) {
        this.listener = listener
    }
}