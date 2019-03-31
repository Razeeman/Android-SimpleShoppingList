package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.prefs.BasePreferenceHelper
import com.example.util.simpleshoppinglist.data.prefs.ItemsSortType
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.di.ActivityScoped
import java.util.*
import javax.inject.Inject

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 */
@ActivityScoped
class MainPresenter
@Inject constructor(private val itemsRepository: BaseItemsRepository,
                    private val preferenceHelper: BasePreferenceHelper)
    : MainContract.Presenter {

    private var view: MainContract.View? = null

    /**
     * Loads items from repository and forwards them to view.
     */
    override fun loadData() {
        itemsRepository.loadItems(object : BaseItemsRepository.LoadItemsCallback {
            override fun onItemsLoaded(items: List<Item>) {
                var listedItems = 0
                var activeItems = 0
                val hideChecked = preferenceHelper.hideChecked
                val sortType = preferenceHelper.sortBy
                val itemsToShow = ArrayList<Item>()

                for (item in items) {
                    if (item.isListed && (item.isActive || !hideChecked)) {
                        itemsToShow.add(item)
                    }
                    if (item.isListed) listedItems++
                    if (item.isListed && item.isActive) activeItems++
                }

                if (listedItems != 0 && activeItems == 0) itemsRepository.clearAllListed()

                // Lint suppressed because DEFAULT items sort type doesn't change item list.
                @Suppress("UNUSED_EXPRESSION")
                when (sortType) {
                    ItemsSortType.DEFAULT -> false
                    ItemsSortType.BY_NAME -> itemsToShow.sortBy {
                        it.name
                    }
                }

                if (itemsToShow.size != 0) {
                    view?.showItems(itemsToShow)
                } else {
                    view?.showNoListedItemsMessage()
                }
            }
            override fun onDataNotAvailable() {
                view?.showNoItemsMessage()
            }
        })
    }

    override fun loadMenuData() {
        view?.updateMenuHideChecked(preferenceHelper.hideChecked)
    }

    override fun removeItemFromList(id: String) {
        itemsRepository.updateItemListed(id, false)
        itemsRepository.updateItemActive(id, true)
        loadData()
    }

    override fun toggleActiveStatus(item: Item) {
        itemsRepository.updateItemActive(item.id, !item.isActive)
        loadData()
    }

    override fun clearList() {
        itemsRepository.clearAllListed()
        loadData()
        view?.showListClearedMessage()
    }

    override fun togglePrefHideChecked() {
        val newValue = !preferenceHelper.hideChecked
        preferenceHelper.hideChecked = newValue
        view?.updateMenuHideChecked(newValue)
        loadData()
    }

    override fun setPrefSortType(sortType: ItemsSortType) {
        preferenceHelper.sortBy = sortType
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