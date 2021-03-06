package com.example.util.simpleshoppinglist.ui.recent

import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.prefs.AppThemeType
import com.example.util.simpleshoppinglist.data.prefs.BasePreferenceHelper
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.di.ActivityScoped
import java.util.*
import javax.inject.Inject

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 */
@ActivityScoped
class RecentPresenter
@Inject constructor(private val itemsRepository: BaseItemsRepository,
                    private val preferenceHelper: BasePreferenceHelper
)
    : RecentContract.Presenter {

    private var view: RecentContract.View? = null

    override val appTheme: AppThemeType
        get() = preferenceHelper.appTheme

    /**
     * Loads items from repository and forwards them to view.
     */
    override fun loadData() {
        itemsRepository.loadItems(object : BaseItemsRepository.LoadItemsCallback {
            override fun onItemsLoaded(items: List<Item>) {
                val itemsToShow = ArrayList<Item>()
                for (item in items) {
                    if (!item.isListed) {
                        itemsToShow.add(item)
                    }
                }

                itemsToShow.sortBy {
                    it.name
                }

                if (itemsToShow.size != 0) {
                    view?.showItems(itemsToShow)
                } else {
                    view?.showAllItemsListedMessage()
                }
            }
            override fun onDataNotAvailable() {
                view?.showNoItemsMessage()
            }
        })
    }

    override fun addItemToList(item: Item) {
        itemsRepository.updateItemListed(item.id, true)
        itemsRepository.updateItemActive(item.id, true)
        itemsRepository.updateListedTime(item.id, Date())
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