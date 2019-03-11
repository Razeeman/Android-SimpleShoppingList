package com.example.util.simpleshoppinglist.data.repo

import com.example.util.simpleshoppinglist.data.db.ListItemDao
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.util.AppExecutors

/**
 * Implementation of the list items repository.
 *
 * @param executors   Executors pool for disk IO thread and main thread.
 * @param listItemDao Data access object for database.
 */
class ItemsRepository
private constructor(private val executors: AppExecutors, private val listItemDao: ListItemDao)
    : BaseItemsRepository {

    companion object {

        // Repository instance.
        @Volatile private var INSTANCE: ItemsRepository? = null

        // Singleton instantiation.
        fun getInstance(executors: AppExecutors, listItemDao: ListItemDao): ItemsRepository {
            if (INSTANCE == null)
                synchronized(ItemsRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ItemsRepository(executors, listItemDao)
                    }
                }
            return INSTANCE!!
        }
    }

    /**
     * Asynchronously load items from the database fire a callback with response.
     *
     * @param callback A callback to return items on the main thread.
     */
    override fun loadListItems(callback: BaseItemsRepository.LoadItemsCallback) {

    }

    /**
     * Asynchronously load one item from the database fire a callback with response.
     *
     * @param id       Id of the item to load from database.
     * @param callback A callback to return item on the main thread.
     */
    override fun loadListItem(id: String, callback: BaseItemsRepository.LoadItemCallback) {
        executors.diskIO.execute {
            val item = listItemDao.getById(id)
            executors.mainThreadIO.execute {
                if (item != null) {
                    callback.onItemLoaded(item)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    /**
     * Asynchronously save an item to the database.
     *
     * @param listItem An item to save.
     */
    override fun saveListItem(listItem: ListItem) {
        executors.diskIO.execute { listItemDao.insert(listItem) }
    }

    /**
     * Asynchronously delete an item from the database.
     *
     * @param listItem An item to delete.
     */
    override fun deleteListItem(listItem: ListItem) {

    }

    /**
     * Asynchronously delete all item from the database.
     */
    override fun deleteAllListItems() {

    }
}