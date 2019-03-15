package com.example.util.simpleshoppinglist.di

import com.example.util.simpleshoppinglist.ui.additem.AddItemFragment
import com.example.util.simpleshoppinglist.ui.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(fragment: MainFragment)
    fun inject(fragment: AddItemFragment)

}