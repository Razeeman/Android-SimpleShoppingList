package com.example.util.simpleshoppinglist.data.repo

import com.example.util.simpleshoppinglist.data.model.Item

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

    fun updateItemActive(item: Item, isActive: Boolean)

    fun updateNameColor(id:String, name: String, color: Int)

    fun clearAllActive()

    fun deleteItem(id: String)

    fun deleteAllItems()

}