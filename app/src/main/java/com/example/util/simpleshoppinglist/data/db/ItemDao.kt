package com.example.util.simpleshoppinglist.data.db

import androidx.room.*
import com.example.util.simpleshoppinglist.data.model.Item
import java.util.*

/**
 * Data access object for items database table.
 */
@Dao
interface ItemDao {

    /**
     * Select all items.
     *
     * @return list of all items.
     */
    @Query("SELECT * FROM items")
    fun getAll(): List<Item>

    /**
     * Select item with certain id.
     *
     * @param id id of the item to select.
     * @return   item with specified id, null if not found.
     */
    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    fun getById(id: String): Item?

    /**
     * Insert item into database.
     *
     * @param item item to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Item)

    /**
     * Update item in the database.
     *
     * @param item item to update.
     */
    @Update
    fun update(item: Item)

    /**
     * Update the listed status of an item.
     *
     * @param id     id of the item to update.
     * @param listed new status to be set.
     */
    @Query("UPDATE items SET listed = :listed WHERE id = :id")
    fun updateListed(id: String, listed: Boolean)

    /**
     * Update the active status of an item.
     *
     * @param id     id of the item to update.
     * @param active new status to be set.
     */
    @Query("UPDATE items SET active = :active WHERE id = :id")
    fun updateActive(id: String, active: Boolean)

    /**
     * Update the name and color of an item.
     *
     * @param id     id of the item to update.
     * @param name   new name of the item.
     * @param color  new color of the item.
     */
    @Query("UPDATE items SET name = :name, color = :color WHERE id = :id")
    fun updateNameColor(id: String, name: String, color: Int)

    /**
     * Update listed time of an item.
     *
     * @param id   id of the item to update.
     * @param date new listed time of the item.
     */
    @Query("UPDATE items SET listed_time = :date WHERE id = :id")
    fun updateListedTime(id: String, date: Date)

    /**
     * Set the listed status to false on all listed items.
     */
    @Query("UPDATE items SET listed = 0 WHERE listed = 1")
    fun clearAllListed()

    /**
     * Delete item from the database.
     *
     * @param item item to delete.
     */
    @Delete
    fun delete(item: Item)

    /**
     * Delete item from the database by id.
     *
     * @param id Id of the item to delete.
     */
    @Query("DELETE FROM items WHERE id = :id")
    fun deleteById(id: String)

    /**
     * Delete all items from the database.
     */
    @Query("DELETE FROM items")
    fun deleteAll()

}