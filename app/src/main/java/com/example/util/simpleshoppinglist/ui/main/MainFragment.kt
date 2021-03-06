package com.example.util.simpleshoppinglist.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.prefs.ItemsSortType
import com.example.util.simpleshoppinglist.ui.additem.AddItemContract
import com.example.util.simpleshoppinglist.ui.additem.AddItemDialogFragment
import com.example.util.simpleshoppinglist.ui.custom.CustomSnackBar
import com.example.util.simpleshoppinglist.ui.custom.ItemAdapter
import com.example.util.simpleshoppinglist.util.ThemeManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import javax.inject.Inject

/**
 * Receives UI events and pass them to presenter.
 */
class MainFragment : Fragment(), MainContract.View {

    private lateinit var menu: Menu

    @Inject
    lateinit var presenter: MainContract.Presenter

    private val itemClickListener = object : ItemAdapter.ItemClickListener {
        // TODO view shouldn't know about the model?
        override fun onItemClick(item: Item) {
            presenter.toggleActiveStatus(item)
        }
        override fun onItemLongClick(item: Item) {
            val fragment = AddItemDialogFragment.newInstance(item.id)
            fragment.show(childFragmentManager, null)
        }
    }
    private val addItemCallback = object : AddItemContract.View.AddItemCallback {
        override fun itemSaved(updated: Boolean) {
            presenter.loadData()
            showItemSavedMessage(updated)
        }
        override fun itemNotSavedEmptyName() {
            showIncorrectItemNameError()
        }
        override fun itemNotSavedAlreadyExists() {
            showItemAlreadyExistsMessage()
        }
    }

    private var itemAdapter =
        ItemAdapter(ArrayList(), itemClickListener, itemsCheckable = true)

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
                presenter.removeItemFromList(viewHolder.itemView.tag as String)
            }
        }).attachToRecyclerView(root.rv_items)

        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu_settings, menu)
        inflater.inflate(R.menu.fragment_menu_sort, menu)
        this.menu = menu
        presenter.loadMenuData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_night_mode -> switchNightMode()
            R.id.menu_group_by_color -> presenter.togglePrefGroupByColor()
            R.id.menu_hide_checked -> presenter.togglePrefHideChecked()
            R.id.menu_sort_name -> presenter.setPrefSortType(ItemsSortType.BY_NAME)
            R.id.menu_sort_default -> presenter.setPrefSortType(ItemsSortType.BY_ORDER)
        }
        return false
    }

    override fun showItems(items: List<Item>) {
        rv_items.visibility = View.VISIBLE
        tv_no_items.visibility = View.INVISIBLE
        itemAdapter.items = items
        itemAdapter.notifyDataSetChanged() // TODO expensive, only redraw one item?
    }

    override fun updateMenuNightMode(value: Boolean) {
        menu.findItem(R.id.menu_night_mode).isChecked = value
    }

    override fun updateMenuGroupByColor(value: Boolean) {
        menu.findItem(R.id.menu_group_by_color).isChecked = value
    }

    override fun updateMenuHideChecked(value: Boolean) {
        menu.findItem(R.id.menu_hide_checked).isChecked = value
    }

    override fun showNoItemsMessage() {
        rv_items.visibility = View.INVISIBLE
        tv_no_items.visibility = View.VISIBLE
        tv_no_items.text = getString(R.string.no_items_added)
    }

    override fun showNoListedItemsMessage() {
        rv_items.visibility = View.INVISIBLE
        tv_no_items.visibility = View.VISIBLE
        tv_no_items.text = getString(R.string.no_items_in_the_list)
    }

    override fun showListClearedMessage() {
        CustomSnackBar
            .make(activity!!.bottom_bar, getString(R.string.main_list_cleared))
            .show()
    }

    override fun showItemSavedMessage(updated: Boolean) {
        val message = when (updated) {
            true -> getString(R.string.recent_item_updated_message)
            false -> getString(R.string.recent_item_saved_message)
        }
        CustomSnackBar
            .make(activity!!.bottom_bar, message)
            .show()
    }

    override fun showIncorrectItemNameError() {
        CustomSnackBar
            .make(activity!!.bottom_bar, getString(R.string.recent_incorrect_name))
            .show()
    }

    override fun showItemAlreadyExistsMessage() {
        CustomSnackBar
            .make(activity!!.bottom_bar, getString(R.string.recent_item_already_exists))
            .show()    }

    private fun switchNightMode() {
        presenter.switchTheme()
        ThemeManager.changeTheme(activity!!)
    }
}