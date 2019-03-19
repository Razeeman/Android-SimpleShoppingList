package com.example.util.simpleshoppinglist.ui.main

import com.example.util.simpleshoppinglist.di.ActivityScoped
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    fun inject(fragment: MainFragment)
    fun inject(activity: MainActivity)

}