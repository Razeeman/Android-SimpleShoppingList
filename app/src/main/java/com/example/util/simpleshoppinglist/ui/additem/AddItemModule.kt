package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.di.ActivityScoped
import dagger.Module
import dagger.Provides

@Module
class AddItemModule(fragment: AddItemDialogFragment) {

    private var itemId: String? = fragment.itemId()
    private var shouldLoadData: Boolean = fragment.shouldLoadData()

    @Provides
    @ActivityScoped
    fun provideItemId(): String? {
        return itemId
    }

    @Provides
    @ActivityScoped
    fun provideShouldLoadDataStatus(): Boolean {
        return shouldLoadData
    }

    @Provides
    @ActivityScoped
    fun getBaseAddItemPresenter(addItemPresenter: AddItemPresenter): AddItemContract.Presenter {
        return addItemPresenter
    }

}