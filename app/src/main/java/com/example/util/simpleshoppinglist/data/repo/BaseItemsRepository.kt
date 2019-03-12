package com.example.util.simpleshoppinglist.data.repo

import com.example.util.simpleshoppinglist.data.model.ListItem

/**
 * Repository contract.
 */
interface BaseItemsRepository {

    interface LoadItemsCallback {

        fun onItemsLoaded(items: List<ListItem>)
        fun onDataNotAvailable()

    }

    interface LoadItemCallback {

        fun onItemLoaded(item: ListItem)
        fun onDataNotAvailable()

    }

    fun loadItems(callback: LoadItemsCallback)

    fun loadItem(id: String, callback: LoadItemCallback)

    fun saveItem(listItem: ListItem)

    fun deleteItem(listItem: ListItem)

    fun deleteAllItems()

}