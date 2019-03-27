package com.example.util.simpleshoppinglist.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Model class for shopping list item.
 * Kotlin data class provides equals/hashCode, toString and copy methods.
 */
@Entity(tableName = "list_items")
data class ListItem (
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "color")
    var color: Int = 0) {

    /**
     * True if the task is active (in the main list), false otherwise.
     */
    @ColumnInfo(name = "active")
    var isActive = false

}