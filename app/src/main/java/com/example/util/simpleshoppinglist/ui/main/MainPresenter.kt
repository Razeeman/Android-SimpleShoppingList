package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.data.model.ListItem
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import javax.inject.Inject

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 */
class MainPresenter @Inject constructor(private val itemsRepository: BaseItemsRepository)
    : MainContract.Presenter {

    private var view: MainContract.View? = null

    /**
     * Loads items from repository and forwards it to view.
     */
    override fun loadData() {
        itemsRepository.loadItems(object : BaseItemsRepository.LoadItemsCallback {
            override fun onItemsLoaded(items: List<ListItem>) {
                view!!.showItems(items)
            }
            override fun onDataNotAvailable() {
                view!!.showNoItems()
            }
        })
    }

    override fun attachView(view: MainContract.View) {
        this.view = view
        loadData()
    }

    override fun detachView() {
        view = null
    }
}