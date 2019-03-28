package com.example.util.simpleshoppinglist.data.model

import org.hamcrest.CoreMatchers.*
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Model tests for getters and setters are probably unnecessary, created for practice purposes.
 */
class ItemTest {

    companion object {

        private const val NEW_ID = "new_id"
        private const val NEW_NAME = "new name"
        private const val NEW_COLOR = 0x123456
        private const val NEW_LISTED = false

    }

    @Test
    fun emptyListItem() {
        val listItem = Item()

        assertThat("Item created with empty name", listItem.name, `is`(""))
        assertThat("Item created with empty color", listItem.color, `is`(0))
        assertThat("Item created with empty color", listItem.isListed, `is`(false))
    }

    @Test
    fun newIdIsNotZero() {
        val listItem = Item()

        assertThat("New id is not zero", UUID.fromString(listItem.id).leastSignificantBits, `is`(not(0L)))
        assertThat("New id is not zero", UUID.fromString(listItem.id).mostSignificantBits, `is`(not(0L)))
    }

    @Test
    fun newIdIsUnique() {
        val item = Item()
        val otherListItem = Item()

        assertThat("New id is unique", item.id, `is`(not(otherListItem.id)))
    }

    @Test
    fun setAndGetName() {
        val item = Item()
        item.name = NEW_NAME

        assertThat("New name is set", item.name, `is`(NEW_NAME))
    }

    @Test
    fun setAndGetColor() {
        val item = Item()
        item.color = NEW_COLOR

        assertThat("New color is set", item.color, `is`(NEW_COLOR))
    }

    @Test
    fun setAndGetListed() {
        val item = Item()
        item.isListed = true

        assertThat("Listed status is set", item.isListed, `is`(true))
    }

    @Test
    fun equals() {
        val item = Item(NEW_ID, NEW_NAME, NEW_COLOR, NEW_LISTED)
        val otherItem = Item(NEW_ID, NEW_NAME, NEW_COLOR, NEW_LISTED)

        assertThat("Items are the same", item, `is`(otherItem))
    }
}