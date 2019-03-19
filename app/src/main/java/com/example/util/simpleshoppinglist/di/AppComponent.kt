package com.example.util.simpleshoppinglist.di

import com.example.util.simpleshoppinglist.ui.additem.AddItemFragment
import com.example.util.simpleshoppinglist.ui.main.MainComponent
import com.example.util.simpleshoppinglist.ui.main.MainModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun plusMainComponent(mainModule: MainModule): MainComponent

    fun inject(fragment: AddItemFragment)

}