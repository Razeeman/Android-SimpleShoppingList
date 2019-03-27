package com.example.util.simpleshoppinglist.ui.custom

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
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
class ItemHolder(inflater: LayoutInflater, private val parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_layout, parent, false)) {

    fun bind(item: Item, listener: ItemAdapter.ItemClickListener) {
        val drawable = ContextCompat.getDrawable(parent.context.applicationContext, R.drawable.item_drawable)
        drawable?.colorFilter = PorterDuffColorFilter(item.color, PorterDuff.Mode.SRC_IN)

        itemView.tag = item.id

        itemView.tv_item.apply {
            text = item.name
            background = drawable
            setOnClickListener {
                if (item.isActive) {
                    listener.onActiveItemClick(item)
                } else {
                    listener.onNonActiveItemClick(item)
                }
            }
            setOnLongClickListener {
                listener.onItemLongClick(item)
                false
            }
        }
    }

}
