package com.example.util.simpleshoppinglist.ui.custom

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.util.simpleshoppinglist.R
import kotlinx.android.synthetic.main.additem_dialog.view.*

class AddItemDialogFragment: AppCompatDialogFragment() {

    private var listener: ButtonClickListener? = null

    interface ButtonClickListener {

        fun onPositiveButton(name: String, color: Int)
        fun onNegativeButton()

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.additem_dialog, null)
        val tvItemName = dialogView.tv_item_name.apply { requestFocus() }
        val itemColor = resources.getColor(R.color.colorPrimary)

        val dialog = AlertDialog.Builder(activity!!).apply {
            setView(dialogView)
            setTitle(getString(R.string.additem_dialog_title))
            setNegativeButton(getString(R.string.additem_dialog_negative)) { _, _ ->
                listener?.onNegativeButton()
            }
            setPositiveButton(getString(R.string.additem_dialog_positive)) { _, _ ->
                listener?.onPositiveButton(tvItemName.text.toString(), itemColor)
            }
        }.create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        return dialog
    }

    fun setListener(listener: ButtonClickListener) {
        this.listener = listener
    }
}