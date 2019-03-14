package com.example.util.simpleshoppinglist

import android.app.Application
import com.example.util.simpleshoppinglist.di.AppComponent
import com.example.util.simpleshoppinglist.di.AppModule
import com.example.util.simpleshoppinglist.di.DaggerAppComponent

class App: Application() {

    companion object {

        private lateinit var appComponent: AppComponent

        fun getAppComponent(): AppComponent {
            return appComponent
        }

    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}