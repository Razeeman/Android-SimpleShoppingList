package com.example.util.simpleshoppinglist.data.repo

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.util.AppExecutors
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
        private val DEFAULT_ITEM = ListItem(name = DEFAULT_NAME, color = DEFAULT_COLOR)

        private const val OTHER_NAME = "other name"
        private const val OTHER_COLOR = 0xABCDEF
        private val OTHER_ITEM = ListItem(name = OTHER_NAME, color = OTHER_COLOR)

    }

    private lateinit var itemsRepository: ItemsRepository
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java).build()
        val executors = AppExecutors()
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
            saveListItem(DEFAULT_ITEM)
            saveListItem(OTHER_ITEM)
            loadListItems(object: BaseItemsRepository.LoadItemsCallback {
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
            saveListItem(DEFAULT_ITEM)
            loadListItem(DEFAULT_ITEM.id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: ListItem) {
                    assertThat(item, `is`(DEFAULT_ITEM))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }

    @Test
    fun saveAndReplaceOnConflict() {
        val newListItem = ListItem(DEFAULT_ITEM.id, OTHER_NAME, OTHER_COLOR)
        itemsRepository.apply {
            saveListItem(DEFAULT_ITEM)
            saveListItem(newListItem)
            loadListItems(object : BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<ListItem>) {
                    assertThat(items.size, `is`(1))
                    assertThat(items[0], `is`(DEFAULT_ITEM))
                    assertThat(items[0].name, `is`(OTHER_NAME))
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
            saveListItem(DEFAULT_ITEM)
            saveListItem(OTHER_ITEM)
            deleteListItem(DEFAULT_ITEM)
            loadListItems(object: BaseItemsRepository.LoadItemsCallback {
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
            saveListItem(DEFAULT_ITEM)
            saveListItem(OTHER_ITEM)
            deleteAllListItems()
            loadListItems(object: BaseItemsRepository.LoadItemsCallback {
                override fun onItemsLoaded(items: List<ListItem>) {
                    assertThat(items.size, `is`(0))
                }
                override fun onDataNotAvailable() {
                    fail("Data not available")
                }
            })
        }
    }
}