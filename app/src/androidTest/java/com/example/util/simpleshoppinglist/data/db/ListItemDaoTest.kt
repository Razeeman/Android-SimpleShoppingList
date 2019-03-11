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
        val DEFAULT_ITEM = ListItem(name = DEFAULT_NAME, color = DEFAULT_COLOR)

        private const val OTHER_NAME = "other name"
        private const val OTHER_COLOR = 0xABCDEF
        val OTHER_ITEM = ListItem(name = OTHER_NAME, color = OTHER_COLOR)

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
        database.listItemDao().insert(DEFAULT_ITEM)
        DEFAULT_ITEM.name = OTHER_NAME
        database.listItemDao().update(DEFAULT_ITEM)
        val fromDatabase = database.listItemDao().getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase?.name, `is`(OTHER_NAME))
        assertThat(fromDatabase?.color, `is`(DEFAULT_COLOR))
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
    fun deleteAll() {
        val fromDatabase = database.listItemDao().apply {
            insert(DEFAULT_ITEM)
            insert(OTHER_ITEM)
            deleteAll() }
            .getAll()

        assertThat(fromDatabase.size, `is`(0))
    }
}