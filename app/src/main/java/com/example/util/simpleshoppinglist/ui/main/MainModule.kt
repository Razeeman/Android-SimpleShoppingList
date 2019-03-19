package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.di.ActivityScoped
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @ActivityScoped
    fun getBaseMainPresenter(mainPresenter: MainPresenter): MainContract.Presenter {
        return mainPresenter
    }

}