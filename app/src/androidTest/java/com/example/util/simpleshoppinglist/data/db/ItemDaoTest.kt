package com.example.util.simpleshoppinglist.data.db

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.util.simpleshoppinglist.data.model.Item
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {

    companion object {

        private const val DEFAULT_NAME = "item name"
        private const val DEFAULT_COLOR = 0xFFFFFF
        private const val DEFAULT_LISTED = false
        private const val DEFAULT_ACTIVE = false
        private val DEFAULT_LISTED_TIME = Date(1000L)
        private val DEFAULT_ITEM = Item(name = DEFAULT_NAME, color = DEFAULT_COLOR,
            isListed = DEFAULT_LISTED, isActive = DEFAULT_ACTIVE)
            .apply { listedTime = DEFAULT_LISTED_TIME }

        private const val OTHER_NAME = "other name"
        private const val OTHER_COLOR = 0xABCDEF
        private const val OTHER_LISTED = true
        private const val OTHER_ACTIVE = true
        private val OTHER_LISTED_TIME = Date(2000L)
        private val OTHER_ITEM = Item(name = OTHER_NAME, color = OTHER_COLOR,
            isListed = OTHER_LISTED, isActive = OTHER_ACTIVE)
            .apply { listedTime = OTHER_LISTED_TIME }

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
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM) }
            .getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase, `is`(DEFAULT_ITEM))
    }

    @Test
    fun insertAndGetById2() {
        val fromDatabase = database.itemDao().apply {
            insert(OTHER_ITEM) }
            .getById(OTHER_ITEM.id)

        assertThat(fromDatabase, `is`(OTHER_ITEM))
    }

    @Test
    fun insertAndReplaceOnConflict() {
        val newListItem = Item(DEFAULT_ITEM.id, OTHER_NAME, OTHER_COLOR)
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM)
            insert(newListItem) }
            .getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase, `is`(newListItem))
    }

    @Test
    fun insertOneAndGetAll() {
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM) }
            .getAll()

        assertThat(fromDatabase.size, `is`(1))
        assertThat(fromDatabase[0], `is`(DEFAULT_ITEM))
    }

    @Test
    fun getAll() {
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM)
            insert(OTHER_ITEM) }
            .getAll()

        assertThat(fromDatabase.size, `is`(2))
    }

    @Test
    fun update() {
        val newItem = DEFAULT_ITEM.copy()
        database.itemDao().insert(newItem)
        newItem.name = OTHER_NAME
        database.itemDao().update(newItem)
        val fromDatabase = database.itemDao().getById(newItem.id)

        assertThat(fromDatabase!!.name, `is`(OTHER_NAME))
        assertThat(fromDatabase.color, `is`(DEFAULT_COLOR))
    }

    @Test
    fun updateListed() {
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM)
            updateListed(DEFAULT_ITEM.id, true) }
            .getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase!!.isListed, `is`(true))
    }

    @Test
    fun updateActive() {
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM)
            updateActive(DEFAULT_ITEM.id, true) }
            .getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase!!.isActive, `is`(true))
    }

    @Test
    fun updateNameColor() {
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM)
            updateNameColor(DEFAULT_ITEM.id, OTHER_NAME, OTHER_COLOR) }
            .getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase!!.name, `is`(OTHER_NAME))
        assertThat(fromDatabase.color, `is`(OTHER_COLOR))
    }

    @Test
    fun updateListedTime() {
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM)
            updateListedTime(DEFAULT_ITEM.id, OTHER_LISTED_TIME) }
            .getById(DEFAULT_ITEM.id)

        assertThat(fromDatabase!!.listedTime, `is`(OTHER_LISTED_TIME))
    }

    @Test
    fun clearAllListed() {
        val newItem = DEFAULT_ITEM.copy().apply { isListed = true }
        val fromDatabase = database.itemDao().apply {
            insert(newItem)
            insert(OTHER_ITEM)
            clearAllListed()
        }.getById(newItem.id)

        assertThat(fromDatabase!!.isListed, `is`(false))
    }

    @Test
    fun clearAllListed2() {
        val newItem1 = DEFAULT_ITEM.copy().apply { isListed = true }
        val newItem2 = OTHER_ITEM.copy().apply { isListed = true }
        val fromDatabase = database.itemDao().apply {
            insert(newItem1)
            insert(newItem2)
            clearAllListed()
        }.getAll()

        assertThat(fromDatabase[0].isListed, `is`(false))
        assertThat(fromDatabase[1].isListed, `is`(false))
    }

    @Test
    fun delete() {
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM)
            insert(OTHER_ITEM)
            delete(DEFAULT_ITEM) }
            .getAll()

        assertThat(fromDatabase.size, `is`(1))
        assertThat(fromDatabase[0], `is`(OTHER_ITEM))
    }

    @Test
    fun deleteById() {
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM)
            insert(OTHER_ITEM)
            deleteById(DEFAULT_ITEM.id)
        }.getAll()

        assertThat(fromDatabase.size, `is`(1))
        assertThat(fromDatabase[0], `is`(OTHER_ITEM))
    }

    @Test
    fun deleteAll() {
        val fromDatabase = database.itemDao().apply {
            insert(DEFAULT_ITEM)
            insert(OTHER_ITEM)
            deleteAll() }
            .getAll()

        assertThat(fromDatabase.size, `is`(0))
    }
}