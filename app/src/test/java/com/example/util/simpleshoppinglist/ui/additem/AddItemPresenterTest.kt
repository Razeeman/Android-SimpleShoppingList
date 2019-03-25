package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.argumentCaptor
import com.example.util.simpleshoppinglist.capture
import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.*

class AddItemPresenterTest {

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
    fun saveItem() {
        // With new item.
        val newItem = ListItem(name = "Name", color = 123)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(newItem.name, newItem.color)

        // Then repository called with these name and color.
        val captor = argumentCaptor<ListItem>()
        Mockito.verify(repository).saveItem(capture(captor))
        assertThat(captor.value.name, CoreMatchers.`is`(newItem.name))
        assertThat(captor.value.color, CoreMatchers.`is`(newItem.color))
        // Then view called to show a message
        Mockito.verify(view).showItemSavedMessage()
    }

    @Test
    fun saveItemWithEmptyName() {
        // With new item.
        val newItem = ListItem(name = "   \n   ", color = 123)

        // When presenter called to save item with certain name and color.
        presenter.saveItem(newItem.name, newItem.color)

        // Then view is called to show error message.
        Mockito.verify(view).showIncorrectItemNameError()
        // Then repository not called to save this item
        val captor = argumentCaptor<ListItem>()
        Mockito.verify(repository, Mockito.times(0)).saveItem(capture(captor))
    }
}