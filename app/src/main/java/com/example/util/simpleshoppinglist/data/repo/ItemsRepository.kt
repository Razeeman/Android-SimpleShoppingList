package com.example.util.simpleshoppinglist.data.repo

import com.example.util.simpleshoppinglist.data.db.ItemDao
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.util.AppExecutors
import java.util.*

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
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ItemsRepository(executors, itemDao).also { INSTANCE = it }
            }
        }

        fun clearInstance() {
            INSTANCE = null
        }
    }

    // Cache for items in the form of <item.id, item>.
    // LinkedHashMap to preserve order of insertion.
    private var itemsCache = LinkedHashMap<String, Item>()

    /**
     * Asynchronously load items from the database and fire a callback with response.
     *
     * @param callback A callback to return items to the caller.
     */
    override fun loadItems(callback: BaseItemsRepository.LoadItemsCallback) {
        // Return cached items if available.
        if (!itemsCache.isEmpty()) {
            callback.onItemsLoaded(itemsCache.values.toList())
            return
        }

        executors.diskIO.execute {
            val items = itemDao.getAll()
            executors.mainThreadIO.execute {
                if (items.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onItemsLoaded(items)
                    items.forEach { itemsCache[it.id] = it }
                }
            }
        }
    }

    /**
     * Asynchronously load one item from the database and fire a callback with response.
     *
     * @param id       Id of the item to load from database.
     * @param callback A callback to return item to the caller.
     */
    override fun loadItem(id: String, callback: BaseItemsRepository.LoadItemCallback) {
        // Return cached item if available.
        val cachedItem = itemsCache[id]
        if (cachedItem != null) {
            callback.onItemLoaded(cachedItem)
            return
        }

        executors.diskIO.execute {
            val item = itemDao.getById(id)
            executors.mainThreadIO.execute {
                if (item != null) {
                    callback.onItemLoaded(item)
                    itemsCache[item.id] = item
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
        itemsCache[item.id] = item
    }

    /**
     * Asynchronously updates an item in the database.
     *
     * @param item An item to update.
     */
    override fun updateItem(item: Item) {
        executors.diskIO.execute { itemDao.update(item) }
        itemsCache[item.id] = item
    }

    /**
     * Asynchronously updates a listed status of an item in the database.
     *
     * @param id       An id of the item to update.
     * @param listed   New status to set.
     */
    override fun updateItemListed(id: String, listed: Boolean) {
        executors.diskIO.execute { itemDao.updateListed(id, listed) }
        itemsCache[id]?.isListed = listed
    }

    /**
     * Asynchronously updates an active status of an item in the database.
     *
     * @param id       An id of the item to update.
     * @param active   New status to set.
     */
    override fun updateItemActive(id: String, active: Boolean) {
        executors.diskIO.execute { itemDao.updateActive(id, active) }
        itemsCache[id]?.isActive = active
    }

    /**
     * Asynchronously updates name and color of an item in the database.
     *
     * @param id    Id of the item to update.
     * @param name  New name to set.
     * @param color New color to set.
     */
    override fun updateNameColor(id: String, name: String, color: Int) {
        executors.diskIO.execute { itemDao.updateNameColor(id, name, color) }
        itemsCache[id]?.apply {
            this.name = name
            this.color = color
        }
    }

    /**
     * Asynchronously updates listed time of an item in the database.
     *
     * @param id   An id of the item to update.
     * @param date New listed time of the item.
     */
    override fun updateListedTime(id: String, date: Date) {
        executors.diskIO.execute { itemDao.updateListedTime(id, date) }
        itemsCache[id]?.listedTime = date
    }

    /**
     * Asynchronously set listed status to false on all items in the list.
     */
    override fun clearAllListed() {
        executors.diskIO.execute { itemDao.clearAllListed() }
        itemsCache.forEach { (_, item) -> item.isListed = false }
    }

    /**
     * Asynchronously delete an item from the database.
     *
     * @param id Id of an item to delete.
     */
    override fun deleteItem(id: String) {
        executors.diskIO.execute { itemDao.deleteById(id) }
        itemsCache.remove(id)
    }

    /**
     * Asynchronously delete all item from the database.
     */
    override fun deleteAllItems() {
        executors.diskIO.execute { itemDao.deleteAll() }
        itemsCache.clear()
    }
}