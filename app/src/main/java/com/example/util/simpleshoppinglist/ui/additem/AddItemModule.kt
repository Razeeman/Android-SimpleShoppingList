package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.di.ActivityScoped
import dagger.Module
import dagger.Provides

@Module
class AddItemModule {

    @Provides
    @ActivityScoped
    fun getBaseAddItemPresenter(addItemPresenter: AddItemPresenter): AddItemContract.Presenter {
        return addItemPresenter
    }

}