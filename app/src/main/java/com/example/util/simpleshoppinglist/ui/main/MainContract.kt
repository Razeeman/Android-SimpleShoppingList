package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.BasePresenter
import com.example.util.simpleshoppinglist.BaseView
import com.example.util.simpleshoppinglist.data.model.ListItem

/**
 * Contract between view and presenter.
 */
interface MainContract {

    interface View: BaseView {

        fun showItems(items: List<ListItem>)

        fun showNoItems()

    }

    interface Presenter: BasePresenter<View> {

        fun loadData()

    }

}