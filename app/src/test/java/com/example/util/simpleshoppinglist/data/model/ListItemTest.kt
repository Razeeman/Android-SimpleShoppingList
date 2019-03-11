package com.example.util.simpleshoppinglist.data.model

import org.hamcrest.CoreMatchers.*
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Model tests for getters and setters are probably unnecessary, created for practice purposes.
 */
class ListItemTest {

    @Test
    fun emptyListItem() {
        val listItem = ListItem()

        assertThat("Empty list item created", listItem.name, `is`(""))
        assertThat("Empty list item created", listItem.color, `is`(0))
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
    fun setId() {
        val listItem = ListItem()
        val newId = UUID.randomUUID().toString()
        listItem.id = newId

        assertThat("New id is set", listItem.id, `is`(newId))
    }

    @Test
    fun getName() {
        val listItem = ListItem()

        assertThat(listItem.name, `is`(""))
    }

    @Test
    fun setName() {
        val listItem = ListItem()
        val newName = "new test name"
        listItem.name = newName

        assertThat("New name is set", listItem.name, `is`(newName))
    }

    @Test
    fun getColor() {
        val listItem = ListItem()

        assertThat(listItem.color, `is`(0))
    }

    @Test
    fun setColor() {
        val listItem = ListItem()
        val newColor = 0x123456
        listItem.color = newColor

        assertThat("New color is set", listItem.color, `is`(newColor))
    }

    // TODO test equals()
}