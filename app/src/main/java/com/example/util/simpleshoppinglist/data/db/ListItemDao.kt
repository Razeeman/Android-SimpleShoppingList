package com.example.util.simpleshoppinglist.data.db

import androidx.room.*
import com.example.util.simpleshoppinglist.data.model.ListItem

/**
 * Data access object for list items database table.
 */
@Dao
interface ListItemDao {

    /**
     * Select all list items.
     *
     * @return list of all items.
     */
    @Query("SELECT * FROM list_items")
    fun getAll(): List<ListItem>

    /**
     * Select list item with certain id.
     *
     * @param id id of the item to select.
     * @return   item with specified id, null if not found.
     */
    @Query("SELECT * FROM list_items WHERE id = :id LIMIT 1")
    fun getById(id: String): ListItem?

    /**
     * Insert list item into database.
     *
     * @param listItem item to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(listItem: ListItem)

    /**
     * Update list item in the database.
     *
     * @param listItem item to update.
     */
    @Update
    fun update(listItem: ListItem)

    /**
     * Update the active status of an item.
     *
     * @param id     id of the item to update.
     * @param active status to be set.
     */
    @Query("UPDATE list_items SET active = :active WHERE id = :id")
    fun updateActive(id: String, active: Boolean)

    /**
     * Set the active status to false on all active items.
     */
    @Query("UPDATE list_items SET active = 0 WHERE active = 1")
    fun clearAllActive()

    /**
     * Delete list item from the database.
     *
     * @param listItem item to delete.
     */
    @Delete
    fun delete(listItem: ListItem)

    /**
     * Delete all list items from the database.
     */
    @Query("DELETE FROM list_items")
    fun deleteAll()

}