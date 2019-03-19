package com.example.util.simpleshoppinglist.data.db

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.util.simpleshoppinglist.data.model.ListItem
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListItemDaoTest {

    companion object {

        private const val DEFAULT_NAME = "item name"
        private const val DEFAULT_COLOR = 0xFFFFFF
        private const val DEFAULT_IS_ACTIVE = false
        private val DEFAULT_ITEM = ListItem(name = DEFAULT_NAME, color = DEFAULT_COLOR)
            .apply { isActive = DEFAULT_IS_ACTIVE }

        private const val OTHER_NAME = "other name"
        private const val OTHER_COLOR = 0xABCDEF
        private const val OTHER_IS_ACTIVE = true
        private val OTHER_ITEM = ListItem(name = OTHER_NAME, color = OTHER_COLOR)
            .apply { isActive = OTHER_IS_ACTIVE }

    }

    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetById() {
        val fromDatabase = database.listItemDao().apply {
            insert(DEFAULT_ITEM) }
            .getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase, `is`(DEFAULT_ITEM))
        assertThat(fromDatabase!!.isActive, `is`(DEFAULT_IS_ACTIVE))
    }

    @Test
    fun insertAndGetById2() {
        val fromDatabase = database.listItemDao().apply {
            insert(OTHER_ITEM) }
            .getById(OTHER_ITEM.id)

        assertThat(fromDatabase, `is`(OTHER_ITEM))
        assertThat(fromDatabase!!.isActive, `is`(OTHER_IS_ACTIVE))
    }

    @Test
    fun insertAndReplaceOnConflict() {
        val newListItem = ListItem(DEFAULT_ITEM.id, OTHER_NAME, OTHER_COLOR)
        val fromDatabase = database.listItemDao().apply {
            insert(DEFAULT_ITEM)
            insert(newListItem) }
            .getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase, `is`(newListItem))
    }

    @Test
    fun insertOneAndGetAll() {
        val fromDatabase = database.listItemDao().apply {
            insert(DEFAULT_ITEM) }
            .getAll()

        assertThat(fromDatabase.size, `is`(1))
        assertThat(fromDatabase[0], `is`(DEFAULT_ITEM))
    }

    @Test
    fun getAll() {
        val fromDatabase = database.listItemDao().apply {
            insert(DEFAULT_ITEM)
            insert(OTHER_ITEM) }
            .getAll()

        assertThat(fromDatabase.size, `is`(2))
    }

    @Test
    fun update() {
        val newItem = DEFAULT_ITEM.copy()
        database.listItemDao().insert(newItem)
        newItem.name = OTHER_NAME
        database.listItemDao().update(newItem)
        val fromDatabase = database.listItemDao().getById(newItem.id)

        assertThat(fromDatabase!!.name, `is`(OTHER_NAME))
        assertThat(fromDatabase.color, `is`(DEFAULT_COLOR))
    }

    @Test
    fun updateActive() {
        val fromDatabase = database.listItemDao().apply {
            insert(DEFAULT_ITEM)
            updateActive(DEFAULT_ITEM.id, true) }
            .getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase!!.isActive, `is`(true))
    }

    @Test
    fun clearAllActive() {
        val newItem = DEFAULT_ITEM.copy().apply { isActive = true }
        val fromDatabase = database.listItemDao().apply {
            insert(newItem)
            insert(OTHER_ITEM)
            clearAllActive()
        }.getById(newItem.id)

        assertThat(fromDatabase!!.isActive, `is`(false))
    }

    @Test
    fun clearAllActive2() {
        val newItem1 = DEFAULT_ITEM.copy().apply { isActive = true }
        val newItem2 = OTHER_ITEM.copy().apply { isActive = true }
        val fromDatabase = database.listItemDao().apply {
            insert(newItem1)
            insert(newItem2)
            clearAllActive()
        }.getAll()

        assertThat(fromDatabase[0].isActive, `is`(false))
        assertThat(fromDatabase[1].isActive, `is`(false))
    }

    @Test
    fun delete() {
        val fromDatabase = database.listItemDao().apply {
            insert(DEFAULT_ITEM)
            insert(OTHER_ITEM)
            delete(DEFAULT_ITEM) }
            .getAll()

        assertThat(fromDatabase.size, `is`(1))
        assertThat(fromDatabase[0], `is`(OTHER_ITEM))
    }

    @Test
    fun deleteById() {
        val fromDatabase = database.listItemDao().apply {
            insert(DEFAULT_ITEM)
            insert(OTHER_ITEM)
            deleteById(DEFAULT_ITEM.id)
        }.getAll()

        assertThat(fromDatabase.size, `is`(1))
        assertThat(fromDatabase[0], `is`(OTHER_ITEM))
    }

    @Test
    fun deleteAll() {
        val fromDatabase = database.listItemDao().apply {
            insert(DEFAULT_ITEM)
            insert(OTHER_ITEM)
            deleteAll() }
            .getAll()

        assertThat(fromDatabase.size, `is`(0))
    }
}