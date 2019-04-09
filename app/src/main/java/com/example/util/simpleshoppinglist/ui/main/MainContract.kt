package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.BasePresenter
import com.example.util.simpleshoppinglist.BaseView
import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.prefs.AppThemeType
import com.example.util.simpleshoppinglist.data.prefs.ItemsSortType

/**
 * Contract between view and presenter.
 */
interface MainContract {

    interface View: BaseView {

        fun showItems(items: List<Item>)

        fun updateMenuNightMode(value: Boolean)

        fun updateMenuGroupByColor(value: Boolean)

        fun updateMenuHideChecked(value: Boolean)

        fun showNoItemsMessage()

        fun showNoListedItemsMessage()

        fun showListClearedMessage()

        fun showItemSavedMessage(updated: Boolean)

        fun showIncorrectItemNameError()

        fun showItemAlreadyExistsMessage()

    }

    interface Presenter: BasePresenter<View> {

        val appTheme: AppThemeType

        fun loadData()

        fun loadMenuData()

        fun removeItemFromList(id: String)

        fun toggleActiveStatus(item: Item)

        fun clearList()

        fun switchTheme()

        fun togglePrefGroupByColor()

        fun togglePrefHideChecked()

        fun setPrefSortType(sortType: ItemsSortType)

    }

}