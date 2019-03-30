package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.BasePresenter
import com.example.util.simpleshoppinglist.BaseView
import com.example.util.simpleshoppinglist.data.model.Item

/**
 * Contract between view and presenter.
 */
interface MainContract {

    interface View: BaseView {

        fun showItems(items: List<Item>)

        fun updateMenuHideChecked(value: Boolean)

        fun showNoItemsMessage()

        fun showNoListedItemsMessage()

        fun showListClearedMessage()

        fun showItemSavedMessage(updated: Boolean)

        fun showIncorrectItemNameError()

    }

    interface Presenter: BasePresenter<View> {

        fun loadData()

        fun loadMenuData()

        fun removeItemFromList(item: Item)

        fun toggleActiveStatus(item: Item)

        fun clearList()

        fun togglePrefHideChecked()

    }

}