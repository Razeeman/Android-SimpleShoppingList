package com.example.util.simpleshoppinglist.data.model

import org.hamcrest.CoreMatchers.*
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Model tests for getters and setters are probably unnecessary, created for practice purposes.
 */
class ListItemTest {

    companion object {

        private const val NEW_ID = "new_id"
        private const val NEW_NAME = "new name"
        private const val NEW_COLOR = 0x123456

    }

    @Test
    fun emptyListItem() {
        val listItem = ListItem()

        assertThat("List item created with empty name", listItem.name, `is`(""))
        assertThat("List item created with empty color", listItem.color, `is`(0))
        assertThat("List item created with empty color", listItem.isActive, `is`(false))
    }

    @Test
    fun newIdIsNotZero() {
        val listItem = ListItem()

        assertThat("New id is not zero", UUID.fromString(listItem.id).leastSignificantBits, `is`(not(0L)))
        assertThat("New id is not zero", UUID.fromString(listItem.id).mostSignificantBits, `is`(not(0L)))
    }

    @Test
    fun newIdIsUnique() {
        val listItem = ListItem()
        val otherListItem = ListItem()

        assertThat("New id is unique", listItem.id, `is`(not(otherListItem.id)))
    }

    @Test
    fun setAndGetName() {
        val listItem = ListItem()
        listItem.name = NEW_NAME

        assertThat("New name is set", listItem.name, `is`(NEW_NAME))
    }

    @Test
    fun setAndGetColor() {
        val listItem = ListItem()
        listItem.color = NEW_COLOR

        assertThat("New color is set", listItem.color, `is`(NEW_COLOR))
    }

    @Test
    fun setAndGetActive() {
        val listItem = ListItem()
        listItem.isActive = true

        assertThat("Active status is set", listItem.isActive, `is`(true))
    }

    @Test
    fun equals() {
        val listItem = ListItem(NEW_ID, NEW_NAME, NEW_COLOR)
        val otherItem = ListItem(NEW_ID, NEW_NAME, NEW_COLOR)
        otherItem.isActive = true

        assertThat("Items are the same", listItem, `is`(otherItem))
    }
}