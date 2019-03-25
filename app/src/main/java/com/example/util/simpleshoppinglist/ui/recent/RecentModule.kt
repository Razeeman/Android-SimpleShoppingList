package com.example.util.simpleshoppinglist.ui.recent

import com.example.util.simpleshoppinglist.di.ActivityScoped
import dagger.Module
import dagger.Provides

@Module
class RecentModule {

    @Provides
    @ActivityScoped
    fun getBaseRecentPresenter(recentPresenter: RecentPresenter): RecentContract.Presenter {
        return recentPresenter
    }

}