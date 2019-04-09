package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.data.model.Item
import com.example.util.simpleshoppinglist.data.prefs.AppThemeType
import com.example.util.simpleshoppinglist.data.prefs.BasePreferenceHelper
import com.example.util.simpleshoppinglist.data.prefs.ItemsSortType
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.di.ActivityScoped
import com.example.util.simpleshoppinglist.util.ColorHSVComparator
import java.util.*
import javax.inject.Inject
import kotlin.Comparator

/**
 * Receives events from UI, loads and saves data in repository and updates UI.
 */
@ActivityScoped
class MainPresenter
@Inject constructor(private val itemsRepository: BaseItemsRepository,
                    private val preferenceHelper: BasePreferenceHelper,
                    private val colorComparator: ColorHSVComparator)
    : MainContract.Presenter {

    private var view: MainContract.View? = null

    override val appTheme: AppThemeType
        get() = preferenceHelper.appTheme

    /**
     * Loads items from repository and forwards them to view.
     */
    override fun loadData() {
        itemsRepository.loadItems(object : BaseItemsRepository.LoadItemsCallback {
            override fun onItemsLoaded(items: List<Item>) {
                var listedItems = 0
                var activeItems = 0
                val hideChecked = preferenceHelper.hideChecked
                val itemsToShow = ArrayList<Item>()

                for (item in items) {
                    if (item.isListed && (item.isActive || !hideChecked)) {
                        itemsToShow.add(item)
                    }
                    if (item.isListed) listedItems++
                    if (item.isListed && item.isActive) activeItems++
                }

                if (hideChecked && listedItems != 0 && activeItems == 0) {
                    itemsRepository.clearAllListed()
                }

                sortItems(itemsToShow)

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
        view?.updateMenuNightMode(preferenceHelper.appTheme == AppThemeType.THEME_DARK)
        view?.updateMenuGroupByColor(preferenceHelper.groupByColor)
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

    override fun switchTheme() {
        val newTheme = when (preferenceHelper.appTheme) {
            AppThemeType.THEME_LIGHT -> AppThemeType.THEME_DARK
            AppThemeType.THEME_DARK -> AppThemeType.THEME_LIGHT
        }
        preferenceHelper.appTheme = newTheme
        view?.updateMenuNightMode(newTheme == AppThemeType.THEME_DARK)
    }

    override fun togglePrefGroupByColor() {
        val newValue = !preferenceHelper.groupByColor
        preferenceHelper.groupByColor = newValue
        view?.updateMenuGroupByColor(newValue)
        loadData()
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

    /**
     * Sort items according to preferences.
     */
    private fun sortItems(itemsToShow: ArrayList<Item>) {
        val sortType = preferenceHelper.sortBy

        // Sort by color or by order of adding in the list.
        var sortComparator: Comparator<Item> = when (sortType) {
            ItemsSortType.DEFAULT -> compareBy { 0 }
            ItemsSortType.BY_NAME -> compareBy { it.name }
        }

        // Sort by color first if needed.
        sortComparator = if (preferenceHelper.groupByColor) {
            compareBy<Item, Int>(colorComparator) { it.color }
                .then(sortComparator)
        } else {
            sortComparator
        }

        itemsToShow.sortWith(sortComparator)
    }
}