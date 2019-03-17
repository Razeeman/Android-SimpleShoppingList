package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import javax.inject.Inject

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 */
class MainPresenter
@Inject constructor(private val itemsRepository: BaseItemsRepository)
    : MainContract.Presenter {

    private var view: MainContract.View? = null

    /**
     * Loads items from repository and forwards them to view.
     */
    override fun loadData() {
        itemsRepository.loadItems(object : BaseItemsRepository.LoadItemsCallback {
            override fun onItemsLoaded(items: List<ListItem>) {
                val itemsToShow = ArrayList<ListItem>()
                for (item in items) {
                    if (item.isActive) {
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

    override fun clearList() {
        itemsRepository.clearAllActive()
        loadData()
    }

    override fun attachView(view: MainContract.View) {
        this.view = view
        loadData()
    }

    override fun detachView() {
        view = null
    }
}