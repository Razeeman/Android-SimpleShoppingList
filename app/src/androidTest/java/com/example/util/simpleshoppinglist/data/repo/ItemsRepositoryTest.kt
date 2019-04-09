package com.example.util.simpleshoppinglist.data.repo

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.util.InstantExecutors
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ItemsRepositoryTest {

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

    private lateinit var itemsRepository: ItemsRepository
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java).build()
        val executors = InstantExecutors()
        itemsRepository = ItemsRepository.getInstance(executors, database.itemDao())
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
    fun loadItems() {
        itemsRepository.apply {
            // With two items saved in the repository.
            saveItem(DEFAULT_ITEM)
            saveItem(OTHER_ITEM)
            // When items loaded.
            loadItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
                    // Then two items are returned.
                    assertThat(items.size, `is`(2))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun saveAndLoadItem() {
        itemsRepository.apply {
            // With item saved in the repository.
            saveItem(DEFAULT_ITEM)
            // When item is loaded.
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    // Then that item is returned.
                    assertThat(item, `is`(DEFAULT_ITEM))
                    assertThat(item.name, `is`(DEFAULT_NAME))
                    assertThat(item.listedTime, `is`(DEFAULT_LISTED_TIME))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun saveAndReplaceOnConflict() {
        // With new item with the same id as the one already saved.
        val newItem = Item(DEFAULT_ITEM.id, OTHER_NAME, OTHER_COLOR)
        itemsRepository.apply {
            saveItem(DEFAULT_ITEM)
            // When this item is saved.
            saveItem(newItem)
            // When items are loaded.
            loadItems(object : BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
                    // Then updated item is returned.
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
    fun saveAndCleanItemName() {
        // With new item with incorrect name.
        val newItem = Item(name = "   Item   \n   NAME \n\n\n\n   2     ", color = DEFAULT_COLOR)
        itemsRepository.apply {
            // When this item is saved.
            saveItem(newItem)
            // When this item is loaded.
            loadItem(newItem.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    // Then items is returned with cleared name.
                    assertThat(item.name, `is`("item name 2"))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun updateItem() {
        // With new item.
        val newItem = DEFAULT_ITEM.copy().apply { listedTime = DEFAULT_LISTED_TIME }
        itemsRepository.apply {
            saveItem(newItem)
            newItem.apply {
                name = OTHER_NAME
                isListed = true
            }
            // When this item is updated.
            updateItem(newItem)
            // When this item is loaded.
            loadItem(newItem.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    // Then updated item is returned.
                    assertThat(item.name, `is`(OTHER_NAME))
                    assertThat(item.isListed, `is`(true))
                    assertThat(item.listedTime, `is`(DEFAULT_LISTED_TIME))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun updateItemListed() {
        itemsRepository.apply {
            // With an item in the repository.
            saveItem(DEFAULT_ITEM)
            // When this item is updated.
            updateItemListed(DEFAULT_ITEM.id, true)
            // When this item is loaded.
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    // Then updated item is returned.
                    assertThat(item.isListed, `is`(true))
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
            // With an item in the repository.
            saveItem(DEFAULT_ITEM)
            // When this item is updated.
            updateItemActive(DEFAULT_ITEM.id, true)
            // When this item is loaded.
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    // Then updated item is returned.
                    assertThat(item.isActive, `is`(true))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun updateNameColor() {
        // With new item.
        val newItem = DEFAULT_ITEM.copy().apply { listedTime = DEFAULT_LISTED_TIME }
        itemsRepository.apply {
            saveItem(newItem)
            newItem.apply {
                name = OTHER_NAME
                color = OTHER_COLOR
            }
            // When this item is updated.
            updateNameColor(DEFAULT_ITEM.id, OTHER_NAME, OTHER_COLOR)
            // When this item is loaded.
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    // Then updated item is returned.
                    assertThat(item, `is`(newItem))
                    assertThat(item.name, `is`(OTHER_NAME))
                    assertThat(item.color, `is`(OTHER_COLOR))
                    assertThat(item.listedTime, `is`(DEFAULT_LISTED_TIME))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun updateListedTime() {
        // With new item.
        val newItem = DEFAULT_ITEM.copy().apply { listedTime = DEFAULT_LISTED_TIME }
        itemsRepository.apply {
            saveItem(newItem)
            newItem.apply {
                listedTime = OTHER_LISTED_TIME
            }
            // When this item is updated.
            updateListedTime(DEFAULT_ITEM.id, OTHER_LISTED_TIME)
            // When this item is loaded.
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    // Then updated item is returned.
                    assertThat(item, `is`(newItem))
                    assertThat(item.name, `is`(DEFAULT_NAME))
                    assertThat(item.color, `is`(DEFAULT_COLOR))
                    assertThat(item.listedTime, `is`(OTHER_LISTED_TIME))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun clearAllListed() {
        // With new item.
        val newItem = DEFAULT_ITEM.copy().apply { isListed = true }
        itemsRepository.apply {
            saveItem(newItem)
            // When all items are unlisted.
            clearAllListed()
            // When items are loaded.
            loadItems(object : BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
                    // Then itens returned with updated status.
                    assertThat(items[0].isListed, `is`(false))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun deleteItem() {
        itemsRepository.apply {
            // With two items in the repository.
            saveItem(DEFAULT_ITEM)
            saveItem(OTHER_ITEM)
            // When one item is deleted.
            deleteItem(DEFAULT_ITEM.id)
            // When items are loaded.
            loadItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
                    // Then only one item is returned.
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
    fun deleteAllItems() {
        itemsRepository.apply {
            // With two items in the repository.
            saveItem(DEFAULT_ITEM)
            saveItem(OTHER_ITEM)
            // When all items are deleted.
            deleteAllItems()
            // When items are loaded.
            loadItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
                    // Then no items is returned.
                    fail("Data not available")
                }
                override fun onDataNotAvailable() {
                    // Test passes.
                }
            })
        }
    }
}