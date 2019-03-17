package com.example.util.simpleshoppinglist.data.repo

import com.example.util.simpleshoppinglist.data.db.ListItemDao
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.util.AppExecutors

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

        fun clearInstance() {
            INSTANCE = null
        }
    }

    /**
     * Asynchronously load items from the database fire a callback with response.
     *
     * @param callback A callback to return items on the main thread.
     */
    override fun loadItems(callback: BaseItemsRepository.LoadItemsCallback) {
        executors.diskIO.execute {
            val items = listItemDao.getAll()
            executors.mainThreadIO.execute {
                if (items.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onItemsLoaded(items)
                }
            }
        }
    }

    /**
     * Asynchronously load one item from the database fire a callback with response.
     *
     * @param id       Id of the item to load from database.
     * @param callback A callback to return item on the main thread.
     */
    override fun loadItem(id: String, callback: BaseItemsRepository.LoadItemCallback) {
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
    override fun saveItem(listItem: ListItem) {
        executors.diskIO.execute { listItemDao.insert(listItem) }
    }

    /**
     * Asynchronously updates an item in the database.
     *
     * @param listItem An item to update.
     */
    override fun updateItem(listItem: ListItem) {
        executors.diskIO.execute { listItemDao.update(listItem) }
    }

    /**
     * Asynchronously updates an active status of an item in the database.
     *
     * @param listItem An item to update.
     * @param isActive New status to set.
     */
    override fun updateItemActive(listItem: ListItem, isActive: Boolean) {
        executors.diskIO.execute { listItemDao.updateActive(listItem.id, isActive)}
    }

    /**
     * Asynchronously set active status to false on all active items.
     */
    override fun clearAllActive() {
        executors.diskIO.execute {
            listItemDao.clearAllActive()
        }
    }

    /**
     * Asynchronously delete an item from the database.
     *
     * @param listItem An item to delete.
     */
    override fun deleteItem(listItem: ListItem) {
        executors.diskIO.execute { listItemDao.delete(listItem)}
    }

    /**
     * Asynchronously delete all item from the database.
     */
    override fun deleteAllItems() {
        executors.diskIO.execute { listItemDao.deleteAll() }
    }
}