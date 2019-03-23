package com.example.util.simpleshoppinglist.ui.additem

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.ui.custom.AddItemDialogFragment
import com.example.util.simpleshoppinglist.ui.custom.ItemAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.additem_fragment.*
import kotlinx.android.synthetic.main.additem_fragment.view.*
import java.util.*
import javax.inject.Inject

class AddItemFragment : Fragment(), AddItemContract.View {

    @Inject
    lateinit var presenter: AddItemContract.Presenter

    private val itemListener = object : ItemAdapter.ItemClickListener {
        override fun onActiveItemClick(activeItem: ListItem) {
            // Do nothing.
        }
        override fun onNonActiveItemClick(nonActiveItem: ListItem) {
            presenter.addItemToList(nonActiveItem)
        }
    }

    private val addItemDialogListener = object : AddItemDialogFragment.ButtonClickListener {
        override fun onPositiveButton(name: String, color: Int) {
            presenter.saveItem(name, color)
        }
        override fun onNegativeButton() {
            // Do nothing.
        }
    }

    private var itemAdapter = ItemAdapter(ArrayList(), itemListener)

    /**
     * Fragment instantiation.
     */
    companion object {
        fun newInstance(): AddItemFragment {
            return AddItemFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.additem_fragment, container, false)

        root.rv_items.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = itemAdapter
        }

        root.tv_add_new_item.setOnClickListener {
            val fragment = AddItemDialogFragment()
            fragment.show(childFragmentManager, null)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                presenter.deleteItem(viewHolder.itemView.tag as String)
            }
        }).attachToRecyclerView(root.rv_items)

        setHasOptionsMenu(true)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.getAddItemComponent().inject(this)
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
            childFragment.setButtonClickListener(addItemDialogListener)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.additem_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> showDeleteAllDialog()
        }
        return true
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

    override fun showItemSavedMessage() {
        Snackbar.make(activity!!.rv_items, getString(R.string.additem_item_saved_message), Snackbar.LENGTH_LONG).show()
    }

    override fun showAllItemsDeletedMessage() {
        Snackbar.make(activity!!.rv_items, getString(R.string.additem_items_deleted), Snackbar.LENGTH_LONG).show()
    }

    override fun showIncorrectItemNameError() {
        Snackbar.make(activity!!.rv_items, getString(R.string.additem_incorrect_name), Snackbar.LENGTH_LONG).show()
    }

    private fun showDeleteAllDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_all_dialog_title))
            .setMessage(getString(R.string.delete_all_dialog_message))
            .setNegativeButton(getString(R.string.delete_all_dialog_negative), null)
            .setPositiveButton(getString(R.string.delete_all_dialog_positive)) { _, _ ->
                presenter.deleteAllItems()
            }
            .create().show()
    }
}