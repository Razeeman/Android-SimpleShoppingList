package com.example.util.simpleshoppinglist.data.repo

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.util.InstantExecutors
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemsRepositoryTest {

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

    private lateinit var itemsRepository: ItemsRepository
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java).build()
        val executors = InstantExecutors()
        itemsRepository = ItemsRepository.getInstance(executors, database.listItemDao())
    }

    @After
    fun tearDown() {
        database.close()
        ItemsRepository.clearInstance()
    }

    @Test
    fun repoNotNull() {
        Assert.assertNotNull(itemsRepository)
    }

    @Test
    fun loadListItems() {
        itemsRepository.apply {
            saveItem(DEFAULT_ITEM)
            saveItem(OTHER_ITEM)
            loadItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<ListItem>) {
                    assertThat(items.size, `is`(2))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun saveAndLoadListItem() {
        itemsRepository.apply {
            saveItem(DEFAULT_ITEM)
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: ListItem) {
                    assertThat(item, `is`(DEFAULT_ITEM))
                    assertThat(item.isActive, `is`(false))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun saveAndReplaceOnConflict() {
        val newItem = ListItem(DEFAULT_ITEM.id, OTHER_NAME, OTHER_COLOR)
        itemsRepository.apply {
            saveItem(DEFAULT_ITEM)
            saveItem(newItem)
            loadItems(object : BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<ListItem>) {
                    assertThat(items.size, `is`(1))
                    assertThat(items[0], `is`(newItem))
                    assertThat(items[0].name, `is`(OTHER_NAME))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun updateItem() {
        val newItem = DEFAULT_ITEM.copy()
        itemsRepository.apply {
            saveItem(newItem)
            newItem.name = OTHER_NAME
            updateItem(newItem)
            loadItem(newItem.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: ListItem) {
                    assertThat(item.name, `is`(OTHER_NAME))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun updateItemActive() {
        itemsRepository.apply {
            saveItem(DEFAULT_ITEM)
            updateItemActive(DEFAULT_ITEM, true)
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: ListItem) {
                    assertThat(item, `is`(DEFAULT_ITEM))
                    assertThat(item.isActive, `is`(true))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun clearAllActive() {
        val newItem = DEFAULT_ITEM.copy().apply { isActive = true }
        itemsRepository.apply {
            saveItem(newItem)
            clearAllActive()
            loadItems(object : BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<ListItem>) {
                    assertThat(items[0].isActive, `is`(false))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun deleteListItem() {
        itemsRepository.apply {
            saveItem(DEFAULT_ITEM)
            saveItem(OTHER_ITEM)
            deleteItem(DEFAULT_ITEM.id)
            loadItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<ListItem>) {
                    assertThat(items.size, `is`(1))
                    assertThat(items[0], `is`(OTHER_ITEM))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun deleteAllListItems() {
        itemsRepository.apply {
            saveItem(DEFAULT_ITEM)
            saveItem(OTHER_ITEM)
            deleteAllItems()
            loadItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<ListItem>) {
                    fail("Data not available")
                }
                override fun onDataNotAvailable() {
                    // Test passes.
                }
            })
        }
    }
}