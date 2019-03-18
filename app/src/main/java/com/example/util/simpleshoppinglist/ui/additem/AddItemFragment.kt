package com.example.util.simpleshoppinglist.ui.additem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

class AddItemFragment : Fragment(), AddItemContract.View, AddItemDialogFragment.ButtonClickListener {

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

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        App.getAppComponent().inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onPositiveButton() {
        // TODO
        presenter.saveItem("New item ${Random().nextInt(999)}",
            resources.getColor(R.color.colorPrimary))
        Snackbar.make(activity!!.rv_items, "Adding some items!", Snackbar.LENGTH_LONG).show()
    }

    override fun onNegativeButton() {
        // Do nothing.
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
}