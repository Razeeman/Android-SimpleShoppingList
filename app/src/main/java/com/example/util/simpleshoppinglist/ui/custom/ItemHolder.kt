package com.example.util.simpleshoppinglist.ui.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.model.Item
import kotlinx.android.synthetic.main.item_layout.view.*

/**
 * Holder for RecyclerView of ListItems.
 */
class ItemHolder(inflater: LayoutInflater, private val parent: ViewGroup,
                 private var itemCheckable: Boolean)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_layout, parent, false)) {

    fun bind(item: Item, listener: ItemAdapter.ItemClickListener) {
        val color = when(itemCheckable and !item.isActive) {
            false -> item.color
            true -> ContextCompat.getColor(parent.context.applicationContext, R.color.grey_500)
        }

        val drawable = ColorStateDrawable(parent.context, R.drawable.item_drawable, color)

        itemView.tag = item.id

        itemView.tv_item.apply {
            text = item.name
            background = drawable
            setOnClickListener {
                listener.onItemClick(item)
            }
            setOnLongClickListener {
                listener.onItemLongClick(item)
                false
            }
        }
    }

}
