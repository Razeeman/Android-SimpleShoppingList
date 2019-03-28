package com.example.util.simpleshoppinglist.data.repo

import com.example.util.simpleshoppinglist.data.db.ItemDao
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.util.AppExecutors

/**
 * Implementation of the items repository.
 *
 * @param executors Executors pool for disk IO thread and main thread.
 * @param itemDao   Data access object for database.
 */
class ItemsRepository
private constructor(private val executors: AppExecutors, private val itemDao: ItemDao)
    : BaseItemsRepository {

    companion object {

        // Repository instance.
        @Volatile private var INSTANCE: ItemsRepository? = null

        // Singleton instantiation.
        fun getInstance(executors: AppExecutors, itemDao: ItemDao): ItemsRepository {
            if (INSTANCE == null)
                synchronized(ItemsRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ItemsRepository(executors, itemDao)
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
            val items = itemDao.getAll()
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
            val item = itemDao.getById(id)
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
     * @param item An item to save.
     */
    override fun saveItem(item: Item) {
        item.name = item.name.replace("\\s+".toRegex(), " ").trim().toLowerCase()
        executors.diskIO.execute { itemDao.insert(item) }
    }

    /**
     * Asynchronously updates an item in the database.
     *
     * @param item An item to update.
     */
    override fun updateItem(item: Item) {
        executors.diskIO.execute { itemDao.update(item) }
    }

    /**
     * Asynchronously updates a listed status of an item in the database.
     *
     * @param item     An item to update.
     * @param listed   New status to set.
     */
    override fun updateItemListed(item: Item, listed: Boolean) {
        executors.diskIO.execute { itemDao.updateListed(item.id, listed)}
    }

    /**
     * Asynchronously updates name and color of an item in the database.
     *
     * @param id    Id of the item to update.
     * @param name  New name to set.
     * @param color New color to set.
     */
    override fun updateNameColor(id: String, name: String, color: Int) {
        executors.diskIO.execute { itemDao.updateNameColor(id, name, color)}
    }

    /**
     * Asynchronously set listed status to false on all items in the list.
     */
    override fun clearAllListed() {
        executors.diskIO.execute {
            itemDao.clearAllListed()
        }
    }

    /**
     * Asynchronously delete an item from the database.
     *
     * @param id Id of an item to delete.
     */
    override fun deleteItem(id: String) {
        executors.diskIO.execute { itemDao.deleteById(id) }
    }

    /**
     * Asynchronously delete all item from the database.
     */
    override fun deleteAllItems() {
        executors.diskIO.execute { itemDao.deleteAll() }
    }
}