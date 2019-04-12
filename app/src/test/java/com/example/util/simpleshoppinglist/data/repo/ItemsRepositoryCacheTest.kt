package com.example.util.simpleshoppinglist.data.repo

import com.example.util.simpleshoppinglist.data.db.ItemDao
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.util.InstantExecutors
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ItemsRepositoryCacheTest {

    companion object {

        private val items = arrayListOf(
        Item(name = "Item 1"),
        Item(name = "Item 2"),
        Item(name = "Item 3", isListed = true, isActive = false),
        Item(name = "Item 4", isListed = true, isActive = true),
        Item(name = "Item 5", isListed = true, isActive = true)
        )
    }

    @Mock private lateinit var dao: ItemDao
    @Mock private lateinit var loadItemsCallback: BaseItemsRepository.LoadItemsCallback
    @Mock private lateinit var loadItemCallback: BaseItemsRepository.LoadItemCallback

    private lateinit var itemsRepository: ItemsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val executors = InstantExecutors()
        itemsRepository = ItemsRepository.getInstance(executors, dao)
    }

    @After
    fun tearDown() {
        ItemsRepository.clearInstance()
    }

    @Test
    fun loadItemsFromCache() {
        // With items in the database.
        doReturn(items).`when`(dao).getAll()

        // When repository is queried two times.
        itemsRepository.loadItems(loadItemsCallback)
        itemsRepository.loadItems(loadItemsCallback)

        // Then database only queried one time.
        verify(dao).getAll()
        verify(loadItemsCallback, times(2)).onItemsLoaded(items)
    }

    @Test
    fun loadItemFromCache() {
        // With item in the database.
        val item = items[0]
        doReturn(item).`when`(dao).getById(item.id)

        // When repository is queried two times.
        itemsRepository.loadItem(item.id, loadItemCallback)
        itemsRepository.loadItem(item.id, loadItemCallback)

        // Then database only queried one time.
        verify(dao).getById(item.id)
        verify(loadItemCallback, times(2)).onItemLoaded(item)
    }
}