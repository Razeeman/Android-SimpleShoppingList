package com.example.util.simpleshoppinglist.ui.recent

import com.example.util.simpleshoppinglist.di.ActivityScoped
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [RecentModule::class])
interface RecentComponent {

    fun inject(fragment: RecentFragment)
    fun inject(activity: RecentActivity)

}