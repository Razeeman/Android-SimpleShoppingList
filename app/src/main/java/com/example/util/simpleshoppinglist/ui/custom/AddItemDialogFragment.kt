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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.util.simpleshoppinglist.R
import kotlinx.android.synthetic.main.additem_dialog.view.*

class AddItemDialogFragment: AppCompatDialogFragment() {

    companion object {

        private const val ITEM_COLOR_BUNDLE_KEY = "item_color"

        private const val DEFAULT_COLOR_ID = R.color.indigo_600
    }

    private var itemColor: Int = 0

    private lateinit var ivItemColor: ImageView

    private var buttonClickListener: ButtonClickListener? = null

    private val colorChangeListener = object: ColorPickerDialog.OnColorChangeListener {
        override fun onColorChanged(color: Int) {
            if (color != itemColor) {
                itemColor = color
                ivItemColor.background.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    /**
     * Interface to listen to dialog button clicks.
     */
    interface ButtonClickListener {

        fun onPositiveButton(name: String, color: Int)
        fun onNegativeButton()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemColor = savedInstanceState?.getInt(ITEM_COLOR_BUNDLE_KEY) ?: itemColor
    }

    // Lint suppressed because dialog doesn't have a view before inflating.
    @Suppress("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.additem_dialog, null)
        val tvItemName = dialogView.tv_item_name.apply { requestFocus() }

        if (itemColor == 0) {
            itemColor = ContextCompat.getColor(context!!, DEFAULT_COLOR_ID)
        }

        ivItemColor = dialogView.iv_item_color.apply {
            background.colorFilter = PorterDuffColorFilter(itemColor, PorterDuff.Mode.SRC_IN)
            setOnClickListener {
                val fragment = ColorPickerDialog.newInstance(itemColor)
                fragment.show(childFragmentManager, null)
            }
        }

        val dialog = AlertDialog.Builder(activity!!).apply {
            setView(dialogView)
            setTitle(getString(R.string.additem_dialog_title))
            setNegativeButton(getString(R.string.additem_dialog_negative)) { _, _ ->
                buttonClickListener?.onNegativeButton()
            }
            setPositiveButton(getString(R.string.additem_dialog_positive)) { _, _ ->
                buttonClickListener?.onPositiveButton(tvItemName.text.toString(), itemColor)
            }
        }.create()

        // Show keyboard right away.
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        return dialog
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ITEM_COLOR_BUNDLE_KEY, itemColor)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        if (childFragment is ColorPickerDialog) {
            childFragment.setOnColorChangeListener(colorChangeListener)
        }
    }

    fun setButtonClickListener(listener: ButtonClickListener) {
        buttonClickListener = listener
    }
}