package com.example.util.simpleshoppinglist.ui.custom

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.util.simpleshoppinglist.R
import com.example.util.simpleshoppinglist.data.model.ListItem
import kotlinx.android.synthetic.main.item_layout.view.*


/**
 * Holder for RecyclerView of ListItems.
 */
class ItemHolder(inflater: LayoutInflater, private val parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_layout, parent, false)) {

    fun bind(listItem: ListItem, listener: ItemAdapter.ItemClickListener) {
        val drawable = ContextCompat.getDrawable(parent.context.applicationContext, R.drawable.item_drawable)
        drawable?.colorFilter = PorterDuffColorFilter(listItem.color, PorterDuff.Mode.SRC_IN)

        itemView.tv_item.apply {
            text = listItem.name
            background = drawable
            setOnClickListener {
                if (listItem.isActive) {
                    listener.onActiveItemClick(listItem)
                } else {
                    listener.onNonActiveItemClick(listItem)
                }
            }
        }
    }

}
