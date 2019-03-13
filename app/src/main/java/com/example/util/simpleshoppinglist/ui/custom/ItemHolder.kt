package com.example.util.simpleshoppinglist.ui.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.model.ListItem
import kotlinx.android.synthetic.main.item_layout.view.*

/**
 * Holder for RecyclerView of ListItems.
 */
class ItemHolder(inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_layout, parent, false)) {

    fun bind(listItem: ListItem) {
        itemView.tv_item.text = listItem.name
    }

}
