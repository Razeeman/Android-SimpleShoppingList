package com.example.util.simpleshoppinglist.ui.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.util.simpleshoppinglist.data.model.Item

/**
 * Adapter for RecyclerView of [Item]s.
 *
 * @param items          List of items.
 * @param listener       Listener to set onto each item.
 * @param itemsCheckable Decides if items should be changed when clicked.
 */
class ItemAdapter(var items: List<Item>, private val listener: ItemClickListener,
                  private var itemsCheckable: Boolean = false)
    : RecyclerView.Adapter<ItemHolder>() {

    // Interface to listen for click events on this item.
    interface ItemClickListener {

        fun onItemClick(item: Item)
        fun onItemLongClick(item: Item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater, parent, itemsCheckable)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(items[position], listener)
    }

}