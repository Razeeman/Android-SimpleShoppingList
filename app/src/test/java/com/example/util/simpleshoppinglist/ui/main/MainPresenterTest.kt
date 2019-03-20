package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.argumentCaptor
import com.example.util.simpleshoppinglist.capture
import com.example.util.simpleshoppinglist.data.model.ListItem
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

        private lateinit var items: List<ListItem>

        @BeforeClass
        @JvmStatic
        fun before() {
            items = arrayListOf(
                ListItem(name = "Item 1"),
                ListItem(name = "Item 2"),
                ListItem(name = "Item 3").apply { isActive = true },
                ListItem(name = "Item 4").apply { isActive = true },
                ListItem(name = "Item 5").apply { isActive = true }
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
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onDataNotAvailable()
        verify(view).showNoItems()
    }

    @Test
    fun loadData_withDataAvailable_noActiveItems() {
        val newItems = arrayListOf(
            ListItem(name = "Item 1"),
            ListItem(name = "Item 2"))

        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(newItems)
        verify(view).showNoItems()
    }

    @Test
    fun loadData_withDataAvailable_withActiveItems() {
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(items)
        val captor = argumentCaptor<List<ListItem>>()
        verify(view).showItems(capture(captor))
        assertThat(captor.value.size, `is`(3))
    }

    @Test
    fun removeItemFromList() {
        val item = ListItem()
        presenter.removeItemFromList(item)

        verify(repository).updateItemActive(item, false)
        // First on attach, second after remove from list.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }

    @Test
    fun clearList() {
        presenter.clearList()

        verify(repository).clearAllActive()
        // First on attach, second after clear list.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }

    @Test
    fun attachView_loadsData() {
        presenter.detachView()
        presenter.attachView(view)

        // First time in setup, second then reattached.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }
}