package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import javax.inject.Inject

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 */
class AddItemPresenter
@Inject constructor(private val itemsRepository: BaseItemsRepository)
    : AddItemContract.Presenter {

    private var view: AddItemContract.View? = null

    override fun attachView(view: AddItemContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }
}