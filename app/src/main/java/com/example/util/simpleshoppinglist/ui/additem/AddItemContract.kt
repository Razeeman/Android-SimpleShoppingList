package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.BasePresenter
import com.example.util.simpleshoppinglist.BaseView

/**
 * Contract between view and presenter.
 */
interface AddItemContract {

    interface View: BaseView {

        interface AddItemCallback {

            fun itemSaved(updated: Boolean)

            fun itemNotSavedEmptyName()

            fun itemNotSavedAlreadyExists()

        }

        fun showItem(name: String, color: Int)

        fun showItemSavedMessage(updated: Boolean)

        fun showIncorrectItemNameError()

        fun showItemAlreadyExistMessage()

    }

    interface Presenter: BasePresenter<View> {

        fun loadItem()

        fun saveItem(name: String, color: Int)

    }

}