package com.example.util.simpleshoppinglist.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.ui.additem.AddItemContract
import com.example.util.simpleshoppinglist.ui.additem.AddItemDialogFragment
import com.example.util.simpleshoppinglist.ui.custom.ItemAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import javax.inject.Inject

class MainFragment : Fragment(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    private val itemListener = object : ItemAdapter.ItemClickListener {
        // TODO view shouldn't know about the model?
        override fun onActiveItemClick(activeItem: ListItem) {
            presenter.removeItemFromList(activeItem)
        }
        override fun onNonActiveItemClick(nonActiveItem: ListItem) {
            // Do nothing.
        }
        override fun onItemLongClick(item: ListItem) {
            val fragment = AddItemDialogFragment.newInstance(item.id)
            fragment.show(childFragmentManager, null)
        }
    }
    private val addItemCallback = object : AddItemContract.View.AddItemCallback {
        override fun itemSaved(updated: Boolean) {
            presenter.loadData()
            showItemSavedMessage(updated)
        }
        override fun itemNotSaved() {
            showIncorrectItemNameError()
        }
    }

    private var itemAdapter = ItemAdapter(ArrayList(), itemListener)

    /**
     * Fragment instantiation.
     */
    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.main_fragment, container, false)

        root.rv_items.apply {
            layoutManager = FlexboxLayoutManager(activity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.CENTER
            }
            adapter = itemAdapter
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.getMainComponent().inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        if (childFragment is AddItemDialogFragment) {
            childFragment.setAddItemCallback(addItemCallback)
        }
    }

    override fun showItems(items: List<ListItem>) {
        rv_items.visibility = View.VISIBLE
        tv_no_items.visibility = View.INVISIBLE
        itemAdapter.items = items
        itemAdapter.notifyDataSetChanged()
    }

    override fun showNoItems() {
        rv_items.visibility = View.INVISIBLE
        tv_no_items.visibility = View.VISIBLE
    }

    override fun showItemSavedMessage(updated: Boolean) {
        val message = when (updated) {
            true -> getString(R.string.recent_item_updated_message)
            false -> getString(R.string.recent_item_saved_message)
        }
        Snackbar.make(activity!!.rv_items, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showIncorrectItemNameError() {
        Snackbar.make(activity!!.rv_items, getString(R.string.recent_incorrect_name), Snackbar.LENGTH_LONG).show()
    }
}