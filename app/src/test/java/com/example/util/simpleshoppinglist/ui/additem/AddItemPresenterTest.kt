package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.argumentCaptor
import com.example.util.simpleshoppinglist.capture
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

class AddItemPresenterTest {

    @Mock private lateinit var repository: BaseItemsRepository
    @Mock private lateinit var view: AddItemContract.View
    @Captor private lateinit var loadItemCallbackCaptor: ArgumentCaptor<BaseItemsRepository.LoadItemCallback>
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
        // With this item.
        val newItem = Item(name = "Item 1")

        // When presenter called to load data.
        presenter.loadItem(newItem.id)

        // Then repository called to load data and callback returned with no data.
        val captor = argumentCaptor<String>()
        verify(repository).loadItem(capture(captor), capture(loadItemCallbackCaptor))
        assertThat(captor.value, `is`(newItem.id))
        loadItemCallbackCaptor.value.onDataNotAvailable()

        // Then ...
        // TODO
    }

    @Test
    fun loadDataWithDataAvailable() {
        // With this item.
        val newItem = Item(name = "Item 1")

        // When presenter called to load data.
        presenter.loadItem(newItem.id)

        // Then repository called to load data and callback returned with this item.
        val captor = argumentCaptor<String>()
        verify(repository).loadItem(capture(captor), capture(loadItemCallbackCaptor))
        assertThat(captor.value, `is`(newItem.id))
        loadItemCallbackCaptor.value.onItemLoaded(newItem)

        // Then this item is shown.
        verify(view).showItem(newItem.name, newItem.color)
    }

    @Test
    fun saveItem() {
        // With new item.
        val newItem = Item(name = "Name", color = 123)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(null, newItem.name, newItem.color)

        // Then repository called to check item name.
        val captor = argumentCaptor<Item>()
        Mockito.verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onDataNotAvailable()
        // Then repository called with these name and color and item is listed and active.
        Mockito.verify(repository).saveItem(capture(captor))
        assertThat(captor.value.name, CoreMatchers.`is`(newItem.name))
        assertThat(captor.value.color, CoreMatchers.`is`(newItem.color))
        assertThat(captor.value.isListed, CoreMatchers.`is`(true))
        assertThat(captor.value.isActive, CoreMatchers.`is`(true))
        // Then view called to show a message
        Mockito.verify(view).showItemSavedMessage(false)
    }

    @Test
    fun saveItem2() {
        // With new item.
        val newItem = Item(name = "Name", color = 123)
        // With another item in repository.
        val anotherItem = Item(name = "Name 2", color = 123)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(null, newItem.name, newItem.color)

        // Then repository called to check item name.
        Mockito.verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(listOf(anotherItem))
        // Then repository called with these name and color and item is listed and active.
        val captor = argumentCaptor<Item>()
        Mockito.verify(repository).saveItem(capture(captor))
        assertThat(captor.value.name, CoreMatchers.`is`(newItem.name))
        assertThat(captor.value.color, CoreMatchers.`is`(newItem.color))
        assertThat(captor.value.isListed, CoreMatchers.`is`(true))
        assertThat(captor.value.isActive, CoreMatchers.`is`(true))
        // Then view called to show a message
        Mockito.verify(view).showItemSavedMessage(false)
    }

    @Test
    fun saveItemWithExistingName() {
        // With new item.
        val newItem = Item(name = "Name", color = 123)
        // With another item in repository with the same name.
        val anotherItem = Item(name = "Name", color = 456)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(null, newItem.name, newItem.color)

        // Then repository called to check item name.
        Mockito.verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(listOf(anotherItem))
        // Then view is called to show error message.
        Mockito.verify(view).showItemAlreadyExistMessage()
        // Then repository not called to save this item
        val captor = argumentCaptor<Item>()
        Mockito.verify(repository, Mockito.times(0)).saveItem(capture(captor))
    }

    @Test
    fun saveItemWithEmptyName() {
        // With new item.
        val newItem = Item(name = "   \n   ", color = 123)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(null, newItem.name, newItem.color)

        // Then view is called to show error message.
        Mockito.verify(view).showIncorrectItemNameError()
        // Then repository not called to save this item
        val captor = argumentCaptor<Item>()
        Mockito.verify(repository, Mockito.times(0)).saveItem(capture(captor))
    }

    @Test
    fun updateItem() {
        // With already existing item.
        val newItem = Item(name = "Name", color = 123)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(newItem.id, newItem.name, newItem.color)

        // Then repository called to check item name.
        Mockito.verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(listOf(newItem))
        // Then repository called to update this item.
        val captor = argumentCaptor<Item>()
        Mockito.verify(repository, times(0)).saveItem(capture(captor))
        Mockito.verify(repository).updateNameColor(newItem.id, newItem.name, newItem.color)
        // Then view called to show a message
        Mockito.verify(view).showItemSavedMessage(true)
    }

    @Test
    fun updateItemWithExistingName() {
        // With already existing item.
        val newItem = Item(name = "Name", color = 123)
        // With another item in repository.
        val anotherItem = Item(name = "Name 2", color = 123)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(newItem.id, anotherItem.name, newItem.color)

        // Then repository called to check item name.
        Mockito.verify(repository).loadItems(capture(loadItemsCallbackCaptor))
        loadItemsCallbackCaptor.value.onItemsLoaded(listOf(anotherItem))
        // Then view is called to show error message.
        Mockito.verify(view).showItemAlreadyExistMessage()
        // Then repository not called to save this item
        val captor = argumentCaptor<Item>()
        Mockito.verify(repository, Mockito.times(0)).saveItem(capture(captor))
    }
}