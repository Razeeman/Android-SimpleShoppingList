package com.example.util.simpleshoppinglist.ui.recent

import com.example.util.simpleshoppinglist.BasePresenter
import com.example.util.simpleshoppinglist.BaseView
import com.example.util.simpleshoppinglist.data.model.Item

/**
 * Contract between view and presenter.
 */
interface RecentContract {

    interface View: BaseView {

        fun showItems(items: List<Item>)

        fun showNoItemsMessage()

        fun showAllItemsListedMessage()

        fun showAllItemsDeletedMessage()

        fun showItemSavedMessage(updated: Boolean)

        fun showIncorrectItemNameError()

        fun showItemAlreadyExistsMessage()

    }

    interface Presenter: BasePresenter<View> {

        fun loadData()

        fun addItemToList(item: Item)

        fun deleteItem(id: String)

        fun deleteAllItems()

    }

}