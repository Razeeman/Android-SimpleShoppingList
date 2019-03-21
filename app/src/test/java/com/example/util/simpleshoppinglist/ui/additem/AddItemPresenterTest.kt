package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.argumentCaptor
import com.example.util.simpleshoppinglist.capture
import com.example.util.simpleshoppinglist.data.model.ListItem
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

class AddItemPresenterTest {

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
    @Mock private lateinit var view: AddItemContract.View
    @Captor private lateinit var loadItemsCallbackCaptor: ArgumentCaptor<BaseItemsRepository.LoadItemsCallback>

    private lateinit var presenter: AddItemPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = AddItemPresenter(repository)
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
            ListItem(name = "Item 1").apply { isActive = true },
            ListItem(name = "Item 2").apply { isActive = true })

        // When view is attached.

        // Then repository called to load data and callback returned with all active items.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(newItems)

        // Then no items are shown.
        verify(view).showNoItems()
    }

    @Test
    fun loadData_withDataAvailable_withNoActiveItems() {
        // When view is attached.

        // Then repository called to load data and callback returned with data.
        verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(items)

        // Then items that are not active are shown.
        val captor = argumentCaptor<List<ListItem>>()
        verify(view).showItems(capture(captor))
        assertThat(captor.value.size, `is`(2))
    }

    @Test
    fun addItemToList() {
        // With new item.
        val newItem = ListItem()

        // When presenter called to add item to the list.
        presenter.addItemToList(newItem)

        // Then repository updates this item to active.
        verify(repository).updateItemActive(newItem, true)
        // Then repository called to reload data. First on attach, second after add to list.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }

    @Test
    fun saveItem() {
        // With new item.
        val newItem = ListItem(name = "Name", color = 123)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(newItem.name, newItem.color)

        // Then repository called with these name and color.
        val captor = argumentCaptor<ListItem>()
        verify(repository).saveItem(capture(captor))
        assertThat(captor.value.name, `is`(newItem.name))
        assertThat(captor.value.color, `is`(newItem.color))
        // Then repository called to reload data. First on attach, second after save.
        verify(repository, times(2)).loadItems(capture(loadItemsCallbackCaptor))
    }

    @Test
    fun saveItemWithEmptyName() {
        // With new item.
        val newItem = ListItem(name = "   \n   ", color = 123)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(newItem.name, newItem.color)

        // Then view is called to show error message.
        verify(view).showIncorrectItemNameError()
        // Then repository not called to save this item
        val captor = argumentCaptor<ListItem>()
        verify(repository, times(0)).saveItem(capture(captor))
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