package com.example.util.simpleshoppinglist.ui.recent

import com.example.util.simpleshoppinglist.argumentCaptor
import com.example.util.simpleshoppinglist.capture
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class RecentPresenterTest {

    companion object {

        private lateinit var items: List<Item>

        @BeforeClass
        @JvmStatic
        fun before() {
            items = arrayListOf(
                Item(name = "Item 1"),
                Item(name = "Item 2"),
                Item(name = "Item 3").apply { isActive = true },
                Item(name = "Item 4").apply { isActive = true },
                Item(name = "Item 5").apply { isActive = true }
            )
        }
    }

    @Mock private lateinit var repository: BaseItemsRepository
    @Mock private lateinit var view: RecentContract.View
    @Captor private lateinit var loadItemsCallbackCaptor: ArgumentCaptor<BaseItemsRepository.LoadItemsCallback>

    private lateinit var presenter: RecentPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = RecentPresenter(repository)
        presenter.attachView(view)
    }

    @After
    fun tearDown() {
        // Not used.
    }

    @Test
    fun loadData_withNoDataAvailable() {
        // When view is attached.

        // Then repository called to load data and callback returned with no data.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onDataNotAvailable()

        // Then no items are shown.
        verify(view).showNoItems()
    }

    @Test
    fun loadData_withDataAvailable_AllActiveItems() {
        // With items that are all active.
        val newItems = arrayListOf(
            Item(name = "Item 1").apply { isActive = true },
            Item(name = "Item 2").apply { isActive = true })

        // When view is attached.

        // Then repository called to load data and callback returned with all active items.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(newItems)

        // Then no items are shown.
        verify(view).showAllItemsActive()
    }

    @Test
    fun loadData_withDataAvailable_withNoActiveItems() {
        // When view is attached.

        // Then repository called to load data and callback returned with data.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(items)

        // Then items that are not active are shown.
        val captor = argumentCaptor<List<Item>>()
        verify(view).showItems(capture(captor))
        assertThat(captor.value.size, `is`(2))
    }

    @Test
    fun addItemToList() {
        // With new item.
        val newItem = Item()

        // When presenter called to add item to the list.
        presenter.addItemToList(newItem)

        // Then repository updates this item to active.
        verify(repository).updateItemActive(newItem, true)
        // Then repository called to reload data. First on attach, second after add to list.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }

    @Test
    fun deleteItem() {
        // When presenter called to delete item with certain id.
        presenter.deleteItem("123")

        // Then repository called with this id.
        verify(repository).deleteItem("123")
        // Then repository called to reload data. First on attach, second after delete.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }

    @Test
    fun deleteAllItems() {
        // When presenter called to delete all items.
        presenter.deleteAllItems()

        // Then repository called to delete all items.
        verify(repository).deleteAllItems()
        // Then repository called to reload data. First on attach, second after delete all.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
        // Then view is called to show a successful message
        verify(view).showAllItemsDeletedMessage()
    }

    @Test
    fun attachView_loadsData() {
        // When view is attached.
        presenter.detachView()
        presenter.attachView(view)

        // Then repository called to reload data. First time in setup, second then reattached.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }
}