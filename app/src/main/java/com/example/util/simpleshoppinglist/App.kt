package com.example.util.simpleshoppinglist

import android.app.Application
import com.example.util.simpleshoppinglist.di.AppComponent
import com.example.util.simpleshoppinglist.di.AppModule
import com.example.util.simpleshoppinglist.di.DaggerAppComponent
import com.example.util.simpleshoppinglist.ui.additem.AddItemComponent
import com.example.util.simpleshoppinglist.ui.additem.AddItemModule
import com.example.util.simpleshoppinglist.ui.main.MainComponent
import com.example.util.simpleshoppinglist.ui.main.MainModule

class App: Application() {

    companion object {

        private lateinit var appComponent: AppComponent
        private var mainComponent: MainComponent? = null
        private var addItemComponent: AddItemComponent? = null

        fun getAppComponent(): AppComponent {
            return appComponent
        }

        fun getMainComponent(): MainComponent {
            return mainComponent ?: appComponent.plusMainComponent(MainModule()).also {
                mainComponent = it
            }
        }

        fun releaseMainComponent() {
            mainComponent = null
        }

        fun getAddItemComponent(): AddItemComponent {
            return addItemComponent ?: appComponent.plusAddItemComponent(AddItemModule()).also {
                addItemComponent = it
            }
        }

        fun releaseAddItemComponent() {
            addItemComponent = null
        }

    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}