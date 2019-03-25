package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.di.ActivityScoped
import javax.inject.Inject

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 */
@ActivityScoped
class AddItemPresenter
@Inject constructor(private val itemsRepository: BaseItemsRepository)
    : AddItemContract.Presenter {

    private var view: AddItemContract.View? = null

    override fun saveItem(name: String, color: Int) {
        // TODO check if already exist and show message
        if (name.isBlank()) {
            view?.showIncorrectItemNameError()
            return
        }
        itemsRepository.saveItem(ListItem(name = name, color = color))
        view?.showItemSavedMessage()
    }

    override fun attachView(view: AddItemContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

}