package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.di.ActivityScoped
import javax.inject.Inject

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 *
 * @param itemId          id of the item to update or null if item is new
 * @param shouldLoadData  flag to determine if data load is needed
 * @param itemsRepository repository of items.
 */
@ActivityScoped
class AddItemPresenter
@Inject constructor(private var itemId: String?,
                    override var shouldLoadData: Boolean,
                    private val itemsRepository: BaseItemsRepository)
    : AddItemContract.Presenter {

    private var view: AddItemContract.View? = null

    override fun loadItem() {
        if (itemId != null) {
            itemsRepository.loadItem(itemId!!, object : BaseItemsRepository.LoadItemCallback {
                override fun onItemLoaded(item: Item) {
                    view?.showItem(item.name, item.color)
                    // Don't load data next time, it should be saved.
                    shouldLoadData = false
                }
                override fun onDataNotAvailable() {
                    throw RuntimeException("Item is being loaded for update but there is no item with this id.")
                }
            })
        }
    }

    override fun saveItem(name: String, color: Int) {
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
                    if (name == item.name && itemId != item.id) {
                        view?.showItemAlreadyExistMessage()
                        return
                    }
                }
                if (itemId == null) {
                    createItem(name, color)
                } else {
                    updateItem(name, color)
                }
            }
            override fun onDataNotAvailable() {
                createItem(name, color)
            }
        })
    }

    override fun attachView(view: AddItemContract.View) {
        this.view = view
        if (itemId != null && shouldLoadData) {
            loadItem()
        }
    }

    override fun detachView() {
        view = null
    }

    private fun createItem(name: String, color: Int) {
        itemsRepository.saveItem(Item(name = name, color = color, isListed = true, isActive = true))
        view?.showItemSavedMessage(false)
    }

    private fun updateItem(name: String, color: Int) {
        itemsRepository.updateNameColor(itemId!!, name, color)
        view?.showItemSavedMessage(true)
    }

}
