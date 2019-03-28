package com.example.util.simpleshoppinglist.data.repo

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.util.InstantExecutors
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
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
        private const val DEFAULT_LISTED = false
        private const val DEFAULT_ACTIVE = false
        private val DEFAULT_ITEM = Item(name = DEFAULT_NAME, color = DEFAULT_COLOR,
            isListed = DEFAULT_LISTED, isActive = DEFAULT_ACTIVE)

        private const val OTHER_NAME = "other name"
        private const val OTHER_COLOR = 0xABCDEF
        private const val OTHER_LISTED = true
        private const val OTHER_ACTIVE = true
        private val OTHER_ITEM = Item(name = OTHER_NAME, color = OTHER_COLOR,
            isListed = OTHER_LISTED, isActive = OTHER_ACTIVE)

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
            saveItem(DEFAULT_ITEM)
            saveItem(OTHER_ITEM)
            loadItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
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
            saveItem(DEFAULT_ITEM)
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    assertThat(item, `is`(DEFAULT_ITEM))
                    assertThat(item.isListed, `is`(false))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun saveAndReplaceOnConflict() {
        val newItem = Item(DEFAULT_ITEM.id, OTHER_NAME, OTHER_COLOR)
        itemsRepository.apply {
            saveItem(DEFAULT_ITEM)
            saveItem(newItem)
            loadItems(object : BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
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
        val newItem = Item(name = "   Item   \n   NAME \n\n\n\n   2     ", color = DEFAULT_COLOR)
        itemsRepository.apply {
            saveItem(newItem)
            loadItem(newItem.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
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
        val newItem = DEFAULT_ITEM.copy()
        itemsRepository.apply {
            saveItem(newItem)
            newItem.apply {
                name = OTHER_NAME
                isListed = true
            }
            updateItem(newItem)
            loadItem(newItem.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    assertThat(item.name, `is`(OTHER_NAME))
                    assertThat(item.isListed, `is`(true))
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
            saveItem(DEFAULT_ITEM)
            updateItemListed(DEFAULT_ITEM, true)
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
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
            saveItem(DEFAULT_ITEM)
            updateItemActive(DEFAULT_ITEM, true)
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
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
        val newItem = DEFAULT_ITEM.copy().apply { isListed = true }
        itemsRepository.apply {
            saveItem(newItem)
            newItem.apply {
                name = OTHER_NAME
                color = OTHER_COLOR
            }
            updateNameColor(DEFAULT_ITEM.id, OTHER_NAME, OTHER_COLOR)
            loadItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    assertThat(item, `is`(newItem))
                    assertThat(item.name, `is`(OTHER_NAME))
                    assertThat(item.color, `is`(OTHER_COLOR))
                    assertThat(item.isListed, `is`(true))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun clearAllListed() {
        val newItem = DEFAULT_ITEM.copy().apply { isListed = true }
        itemsRepository.apply {
            saveItem(newItem)
            clearAllListed()
            loadItems(object : BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
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
            saveItem(DEFAULT_ITEM)
            saveItem(OTHER_ITEM)
            deleteItem(DEFAULT_ITEM.id)
            loadItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
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
            saveItem(DEFAULT_ITEM)
            saveItem(OTHER_ITEM)
            deleteAllItems()
            loadItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<Item>) {
                    fail("Data not available")
                }
                override fun onDataNotAvailable() {
                    // Test passes.
                }
            })
        }
    }
}