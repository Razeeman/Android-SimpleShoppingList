package com.example.util.simpleshoppinglist.ui.custom

import android.app.Dialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.util.simpleshoppinglist.R
import kotlinx.android.synthetic.main.additem_dialog.view.*

class AddItemDialogFragment: AppCompatDialogFragment() {

    // TODO check, refactor this and all child fragments

    private var itemColor: Int = 0

    private lateinit var ivItemColor: ImageView

    private var buttonListener: ButtonClickListener? = null

    private val colorChangeListener = object: ColorPickerDialog.OnColorChangeListener {
        override fun onColorChanged(color: Int) {
            if (color != itemColor) {
                itemColor = color
                ivItemColor.background.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    interface ButtonClickListener {

        fun onPositiveButton(name: String, color: Int)
        fun onNegativeButton()

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.additem_dialog, null)
        val tvItemName = dialogView.tv_item_name.apply { requestFocus() }

        if (itemColor == 0) {
            itemColor = resources.getColor(R.color.indigo_600)
        }

        ivItemColor = dialogView.iv_item_color
        ivItemColor.setOnClickListener {
            val fragment = ColorPickerDialog()
            fragment.init(itemColor)
            fragment.setOnColorChangeListener(colorChangeListener)
            fragment.show(childFragmentManager, null)
        }

        val dialog = AlertDialog.Builder(activity!!).apply {
            setView(dialogView)
            setTitle(getString(R.string.additem_dialog_title))
            setNegativeButton(getString(R.string.additem_dialog_negative)) { _, _ ->
                buttonListener?.onNegativeButton()
            }
            setPositiveButton(getString(R.string.additem_dialog_positive)) { _, _ ->
                buttonListener?.onPositiveButton(tvItemName.text.toString(), itemColor)
            }
        }.create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        return dialog
    }

    fun setListener(listener: ButtonClickListener) {
        buttonListener = listener
    }
}