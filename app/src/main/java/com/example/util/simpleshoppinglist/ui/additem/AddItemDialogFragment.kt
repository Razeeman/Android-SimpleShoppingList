package com.example.util.simpleshoppinglist.ui.additem

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.ui.custom.ColorPickerDialog
import com.example.util.simpleshoppinglist.ui.custom.ColorStateDrawable
import kotlinx.android.synthetic.main.additem_fragment.view.*
import javax.inject.Inject

/**
 * Receives UI events and pass them to presenter.
 */
class AddItemDialogFragment: AppCompatDialogFragment(), AddItemContract.View {

    private var itemId: String? = null
    private var itemColor: Int = 0
    // Flag to determine if data load is needed (first load or configuration change).
    private var shouldLoadData = true
    private lateinit var etItemName: EditText
    private lateinit var ivItemColor: ImageView

    // Callback to pass events to parent fragment.
    private var addItemCallback: AddItemContract.View.AddItemCallback? = null
    /*
    Listener to set onto child fragment.
    Color change event is passed through series of views:
    ColorPick -> ColorPickerPalette -> ColorPickerDialog -> AddItemDialogFragment.
    */
    private val colorChangeListener = object: ColorPickerDialog.OnColorChangeListener {
        override fun onColorChanged(color: Int) {
            if (color != itemColor) {
                itemColor = color
                updateItemColor()
            }
        }
    }

    @Inject
    lateinit var presenter: AddItemContract.Presenter

    companion object {

        private const val ITEM_ID_BUNDLE_KEY = "item_id"
        private const val ITEM_COLOR_BUNDLE_KEY = "item_color"
        private const val LOAD_DATA_BUNDLE_KEY = "load_data"
        private const val DEFAULT_COLOR_ID = R.color.indigo_600

        fun newInstance(id: String?): AddItemDialogFragment {
            val args = Bundle()
            if (id != null) {
                args.putString(ITEM_ID_BUNDLE_KEY, id)
            }
            return AddItemDialogFragment().apply { arguments = args }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemId = arguments?.getString(ITEM_ID_BUNDLE_KEY)
        itemColor = savedInstanceState?.getInt(ITEM_COLOR_BUNDLE_KEY) ?: itemColor
        shouldLoadData = savedInstanceState?.getBoolean(LOAD_DATA_BUNDLE_KEY) ?: shouldLoadData
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        App.getAddItemComponent(this).inject(this)
    }

    // Lint suppressed because dialog doesn't have a view before inflating.
    @Suppress("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.additem_fragment, null)

        // Preparing text information in dialog according to if it is a new item or edited
        val dialogTitle: String
        val nameHint: String
        val colorHint: String
        val negativeButtonText: String
        val positiveButtonText: String
        if (itemId == null) {
            dialogTitle = getString(R.string.additem_dialog_title)
            nameHint = getString(R.string.additem_dialog_name_hint)
            colorHint = getString(R.string.additem_dialog_color_label)
            negativeButtonText = getString(R.string.additem_dialog_negative)
            positiveButtonText = getString(R.string.additem_dialog_positive)
        } else {
            dialogTitle = getString(R.string.edit_item_dialog_title)
            nameHint = getString(R.string.edit_item_dialog_name_hint)
            colorHint = getString(R.string.edit_item_dialog_color_label)
            negativeButtonText = getString(R.string.edit_item_dialog_negative)
            positiveButtonText = getString(R.string.edit_item_dialog_positive)
        }

        // Set default color on first creation.
        if (itemColor == 0) {
            itemColor = ContextCompat.getColor(context!!, DEFAULT_COLOR_ID)
        }

        etItemName = dialogView.et_item_name.apply {
            hint = nameHint
            requestFocus()
        }
        dialogView.tv_color_label.text = colorHint
        ivItemColor = dialogView.iv_item_color.apply {
            setOnClickListener {
                val fragment = ColorPickerDialog.newInstance(itemColor)
                fragment.show(childFragmentManager, null)
            }
        }
        updateItemColor()

        // Building dialog.
        val dialog = AlertDialog.Builder(activity!!).apply {
            setView(dialogView)
            setTitle(dialogTitle)
            setNegativeButton(negativeButtonText) { _, _ ->
                // Do nothing.
            }
            setPositiveButton(positiveButtonText) { _, _ ->
                presenter.saveItem(etItemName.text.toString(), itemColor)
            }
        }.create()

        // Show keyboard right away.
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        return dialog
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        App.releaseAddItemComponent()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ITEM_COLOR_BUNDLE_KEY, itemColor)
        outState.putBoolean(LOAD_DATA_BUNDLE_KEY, presenter.shouldLoadData)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        if (childFragment is ColorPickerDialog) {
            childFragment.setOnColorChangeListener(colorChangeListener)
        }
    }

    override fun showItem(name: String, color: Int) {
        etItemName.apply {
            setText(name)
            setSelection(this.text.length)
        }
        itemColor = color
        updateItemColor()
    }

    override fun showItemSavedMessage(updated: Boolean) {
        addItemCallback?.itemSaved(updated)
    }

    override fun showIncorrectItemNameError() {
        addItemCallback?.itemNotSavedEmptyName()
    }

    override fun showItemAlreadyExistMessage() {
        addItemCallback?.itemNotSavedAlreadyExists()
    }

    fun setAddItemCallback(callback: AddItemContract.View.AddItemCallback) {
        addItemCallback = callback
    }

    fun itemId(): String? {
        return itemId
    }

    fun shouldLoadData(): Boolean {
        return shouldLoadData
    }

    private fun updateItemColor() {
        ivItemColor.background = ColorStateDrawable(context!!, R.drawable.color_pick_drawable, itemColor)
    }

}