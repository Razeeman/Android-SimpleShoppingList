package com.example.util.simpleshoppinglist.data.repo

import com.example.util.simpleshoppinglist.data.model.Item
import java.util.*

/**
 * Repository contract.
 */
interface BaseItemsRepository {

    interface LoadItemsCallback {

        fun onItemsLoaded(items: List<Item>)

        fun onDataNotAvailable()

    }

    interface LoadItemCallback {

        fun onItemLoaded(item: Item)

        fun onDataNotAvailable()

    }

    fun loadItems(callback: LoadItemsCallback)

    fun loadItem(id: String, callback: LoadItemCallback)

    fun saveItem(item: Item)

    fun updateItem(item: Item)

    fun updateItemListed(id: String, listed: Boolean)

    fun updateItemActive(id: String, active: Boolean)

    fun updateNameColor(id:String, name: String, color: Int)

    fun updateListedTime(id: String, date: Date)

    fun clearAllListed()

    fun deleteItem(id: String)

    fun deleteAllItems()

}