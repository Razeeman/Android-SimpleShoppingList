package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.data.model.Item
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
                override fun onItemLoaded(item: Item) {
                    view?.showItem(item.name, item.color)
                }
                override fun onDataNotAvailable() {
                    throw RuntimeException("Item is being loaded for update but there is no item with this id.")
                }
            })
        }
    }

    override fun saveItem(id: String?, name: String, color: Int) {
        // Check if name is empty.
        if (name.isBlank()) {
            view?.showIncorrectItemNameError()
            return
        }

        // Check if name already exists.
        itemsRepository.loadItems(object : BaseItemsRepository.LoadItemsCallback {
            override fun onItemsLoaded(items: List<Item>) {
                for(item in items) {
                    // If different item found with the same name.
                    if (name == item.name && id != item.id) {
                        view?.showItemAlreadyExistMessage()
                        return
                    }
                }
                if (id == null) {
                    saveItem(name, color)
                } else {
                    updateItem(id, name, color)
                }
            }
            override fun onDataNotAvailable() {
                saveItem(name, color)
            }
        })
    }

    override fun attachView(view: AddItemContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    private fun saveItem(name: String, color: Int) {
        itemsRepository.saveItem(Item(name = name, color = color, isListed = true, isActive = true))
        view?.showItemSavedMessage(false)
    }

    private fun updateItem(id: String, name: String, color: Int) {
        itemsRepository.updateNameColor(id, name, color)
        view?.showItemSavedMessage(true)
    }

}
