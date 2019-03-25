package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.BasePresenter
import com.example.util.simpleshoppinglist.BaseView

/**
 * Contract between view and presenter.
 */
interface AddItemContract {

    interface View: BaseView {

        interface AddItemCallback {

            fun itemAdded()
            fun itemNotAdded()

        }

        fun showItemSavedMessage()

        fun showIncorrectItemNameError()

    }

    interface Presenter: BasePresenter<View> {

        fun saveItem(name: String, color: Int)

    }

}