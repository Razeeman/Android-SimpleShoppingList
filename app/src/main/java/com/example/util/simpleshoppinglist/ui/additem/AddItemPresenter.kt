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

    override fun loadItem(id: String?) {
        if (id != null) {
            itemsRepository.loadItem(id, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: ListItem) {
                    view?.showItem(item.name, item.color)
                }
                override fun onDataNotAvailable() {
                    // TODO handle error.
                }
            })
        }
    }

    override fun saveItem(id: String?, name: String, color: Int) {
        // TODO check if already exist and show message
        if (name.isBlank()) {
            view?.showIncorrectItemNameError()
            return
        }
        if (id == null) {
            itemsRepository.saveItem(ListItem(name = name, color = color).apply {
                isActive = true
            })
            view?.showItemSavedMessage(false)
        } else {
            itemsRepository.updateNameColor(id, name, color)
            view?.showItemSavedMessage(true)
        }
    }

    override fun attachView(view: AddItemContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

}