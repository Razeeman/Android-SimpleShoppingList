package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.argumentCaptor
import com.example.util.simpleshoppinglist.capture
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.junit.BeforeClass
import org.mockito.Mockito.times


class MainPresenterTest {

    companion object {

        private lateinit var items: List<Item>

        @BeforeClass
        @JvmStatic
        fun before() {
            items = arrayListOf(
                Item(name = "Item 1"),
                Item(name = "Item 2"),
                Item(name = "Item 3", isListed = true),
                Item(name = "Item 4", isListed = true),
                Item(name = "Item 5", isListed = true)
            )
        }
    }

    @Mock private lateinit var repository: BaseItemsRepository
    @Mock private lateinit var view: MainContract.View
    @Captor private lateinit var loadItemsCallbackCaptor: ArgumentCaptor<BaseItemsRepository.LoadItemsCallback>

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = MainPresenter(repository)
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
        verify(view).showNoItemsMessage()
    }

    @Test
    fun loadData_withDataAvailable_noListedItems() {
        // With no listed items.
        val newItems = arrayListOf(
            Item(name = "Item 1"),
            Item(name = "Item 2"))

        // When view is attached.

        // Then repository called to load data and callback returned with no listed items.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(newItems)

        // Then no items are shown.
        verify(view).showNoListedItemsMessage()
    }

    @Test
    fun loadData_withDataAvailable_withListedItems() {
        // When view is attached.

        // Then repository called to load data and callback returned with data.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(items)

        // Then items that are listed are shown.
        val captor = argumentCaptor<List<Item>>()
        verify(view).showItems(capture(captor))
        assertThat(captor.value.size, `is`(3))
    }

    @Test
    fun removeItemFromList() {
        // With new item.
        val item = Item()

        // When presenter called to remove this item from the list.
        presenter.removeItemFromList(item)

        // Then repository updates this item to not listed.
        verify(repository).updateItemListed(item, false)
        // Then repository called to reload data.First on attach, second after remove from list.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }

    @Test
    fun clearList() {
        // When presenter called to clear the list.
        presenter.clearList()

        // Then repository called to clear all listed items.
        verify(repository).clearAllListed()
        // Then repository called to reload data. First on attach, second after clear list.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
        // Then view called to show a message
        verify(view).showListClearedMessage()
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