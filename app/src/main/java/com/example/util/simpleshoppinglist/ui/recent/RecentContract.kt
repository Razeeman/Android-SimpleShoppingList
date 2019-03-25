package com.example.util.simpleshoppinglist.ui.recent

import com.example.util.simpleshoppinglist.BasePresenter
import com.example.util.simpleshoppinglist.BaseView
import com.example.util.simpleshoppinglist.data.model.ListItem

/**
 * Contract between view and presenter.
 */
interface RecentContract {

    interface View: BaseView {

        fun showItems(items: List<ListItem>)

        fun showNoItems()

        fun showItemSavedMessage()

        fun showAllItemsDeletedMessage()

        fun showIncorrectItemNameError()

    }

    interface Presenter: BasePresenter<View> {

        fun loadData()

        fun addItemToList(item: ListItem)

        fun deleteItem(id: String)

        fun deleteAllItems()

    }

}