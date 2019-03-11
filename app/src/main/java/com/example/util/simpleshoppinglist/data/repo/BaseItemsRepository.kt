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

    fun loadListItems(callback: LoadItemsCallback)

    fun loadListItem(id: String, callback: LoadItemCallback)

    fun saveListItem(listItem: ListItem)

    fun deleteListItem(listItem: ListItem)

    fun deleteAllListItems()

}