package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.argumentCaptor
import com.example.util.simpleshoppinglist.capture
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.prefs.BasePreferenceHelper
import com.example.util.simpleshoppinglist.data.prefs.ItemsSortType
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.junit.BeforeClass
import org.mockito.Mockito.*

class MainPresenterTest {

    companion object {

        private lateinit var items: List<Item>

        @BeforeClass
        @JvmStatic
        fun before() {
            items = arrayListOf(
                Item(name = "Item 1"),
                Item(name = "Item 2"),
                Item(name = "Item 3", isListed = true, isActive = false),
                Item(name = "Item 4", isListed = true, isActive = true),
                Item(name = "Item 5", isListed = true, isActive = true)
            )
        }
    }

    @Mock private lateinit var repository: BaseItemsRepository
    @Mock private lateinit var preferenceHelper: BasePreferenceHelper
    @Mock private lateinit var view: MainContract.View
    @Captor private lateinit var loadItemsCallbackCaptor: ArgumentCaptor<BaseItemsRepository.LoadItemsCallback>

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = MainPresenter(repository, preferenceHelper)
        presenter.attachView(view)

        doReturn(ItemsSortType.DEFAULT).`when`(preferenceHelper).sortBy
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
    fun loadData_withDataAvailable_withHideChecked() {
        // With hide checked preference.
        doReturn(true).`when`(preferenceHelper).hideChecked

        // When view is attached.

        // Then repository called to load data and callback returned with data.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(items)

        // Then items that are listed and active are shown.
        val captor = argumentCaptor<List<Item>>()
        verify(view).showItems(capture(captor))
        assertThat(captor.value.size, `is`(2))
    }

    @Test
    fun loadData_withDataAvailable_withHideChecked_AllChecked() {
        // With hide checked preference.
        doReturn(true).`when`(preferenceHelper).hideChecked

        // With no active items.
        val newItems = arrayListOf(
            Item(name = "Item 1", isListed = true),
            Item(name = "Item 2", isListed = true))

        // When view is attached.

        // Then repository called to load data and callback returned with no active items.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(newItems)

        // Then repository is called to clear the list.
        verify(repository).clearAllListed()
        // Then no items are shown.
        verify(view).showNoListedItemsMessage()
    }

    @Test
    fun loadData_withDataAvailable_withSortByName() {
        // With sort by name preference.
        doReturn(ItemsSortType.BY_NAME).`when`(preferenceHelper).sortBy

        // With unsorted items.
        val newItems = arrayListOf(
            Item(name = "B", isListed = true),
            Item(name = "A", isListed = true),
            Item(name = "C", isListed = true))

        // When view is attached.

        // Then repository called to load data and callback returned with unsorted items.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(newItems)

        // Then items are shown sorted by name.
        val captor = argumentCaptor<List<Item>>()
        verify(view).showItems(capture(captor))
        assertThat(captor.value.size, `is`(3))
        assertThat(captor.value[0], `is`(newItems[1]))
        assertThat(captor.value[1], `is`(newItems[0]))
        assertThat(captor.value[2], `is`(newItems[2]))
    }

    @Test
    fun loadMenuData() {
        // With preference set up.
        doReturn(true).`when`(preferenceHelper).hideChecked

        // When presenter loads menu data.
        presenter.loadMenuData()

        // Then preferences are queried.
        verify(preferenceHelper).hideChecked
        // Then view is called to update menu with this value.
        verify(view).updateMenuHideChecked(true)
    }

    @Test
    fun removeItemFromList() {
        // With new item.
        val item = Item()

        // When presenter called to remove this item from the list.
        presenter.removeItemFromList(item.id)

        // Then repository updates this item to not listed.
        verify(repository).updateItemListed(item.id, false)
        // Then repository updates this item to not active.
        verify(repository).updateItemActive(item.id, true)
        // Then repository called to reload data. First on attach, second after remove from list.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }

    @Test
    fun toggleActiveStatus() {
        // With new not active item.
        val item = Item().apply { isActive = false }

        // When presenter called to update a status of the item.
        presenter.toggleActiveStatus(item)

        // Then repository is called to update this item.
        verify(repository).updateItemActive(item.id, true)
        // Then repository called to reload data. First on attach, second after updating item.
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
    fun togglePrefHideChecked() {
        // With preference set up.
        doReturn(true).`when`(preferenceHelper).hideChecked

        // When presenter called to update preference.
        presenter.togglePrefHideChecked()

        // Then preferences are queried to update preference.
        verify(preferenceHelper).hideChecked
        // Then view is called to update menu.
        verify(view).updateMenuHideChecked(false)
        // Then repository called to reload data. First on attach, second after updating item.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
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