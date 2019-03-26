package com.example.util.simpleshoppinglist.ui.recent

import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.di.ActivityScoped
import javax.inject.Inject

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 */
@ActivityScoped
class RecentPresenter
@Inject constructor(private val itemsRepository: BaseItemsRepository)
    : RecentContract.Presenter {

    private var view: RecentContract.View? = null

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
                if (itemsToShow.size != 0) {
                    view?.showItems(itemsToShow)
                } else {
                    view?.showNoItems()
                }
            }
            override fun onDataNotAvailable() {
                view?.showNoItems()
            }
        })
    }

    override fun addItemToList(item: ListItem) {
        itemsRepository.updateItemActive(item, true)
        loadData()
    }

    override fun deleteItem(id: String) {
        itemsRepository.deleteItem(id)
        loadData()
    }

    override fun deleteAllItems() {
        itemsRepository.deleteAllItems()
        loadData()
        view?.showAllItemsDeletedMessage()
    }

    override fun attachView(view: RecentContract.View) {
        this.view = view
        loadData()
    }

    override fun detachView() {
        view = null
    }
}