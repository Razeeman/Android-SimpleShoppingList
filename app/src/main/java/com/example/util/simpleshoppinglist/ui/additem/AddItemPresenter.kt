package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import javax.inject.Inject

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 */
class AddItemPresenter
@Inject constructor(private val itemsRepository: BaseItemsRepository)
    : AddItemContract.Presenter {

    private var view: AddItemContract.View? = null

    /**
     * Loads items from repository and forwards them to view.
     */
    override fun loadData() {
        itemsRepository.loadItems(object : BaseItemsRepository.LoadItemsCallback {
            override fun onItemsLoaded(items: List<ListItem>) {
                val itemsToShow = ArrayList<ListItem>()
                for (item in items) {
                    if (!item.isActive) {
                        itemsToShow.add(item)
                    }
                }
                view!!.showItems(itemsToShow)
            }
            override fun onDataNotAvailable() {
                view!!.showNoItems()
            }
        })
    }

    override fun addItem(item: ListItem) {
        itemsRepository.updateItemActive(item, true)
        loadData()
    }

    override fun attachView(view: AddItemContract.View) {
        this.view = view
        loadData()
    }

    override fun detachView() {
        view = null
    }
}