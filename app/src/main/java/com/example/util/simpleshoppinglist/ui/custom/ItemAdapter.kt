package com.example.util.simpleshoppinglist.ui.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.util.simpleshoppinglist.data.model.ListItem

/**
 * Adapter for RecyclerView of ListItems.
 */
class ItemAdapter(var items: List<ListItem>, private val listener: ItemClickListener)
    : RecyclerView.Adapter<ItemHolder>() {

    interface ItemClickListener {

        fun onActiveItemClick(activeItem: ListItem)
        fun onNonActiveItemClick(nonActiveItem: ListItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(items[position], listener)
    }

}