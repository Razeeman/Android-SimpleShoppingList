package com.example.util.simpleshoppinglist.di

import android.content.Context
import com.example.util.simpleshoppinglist.App
import com.example.util.simpleshoppinglist.data.db.AppDatabase
import com.example.util.simpleshoppinglist.data.db.ItemDao
import com.example.util.simpleshoppinglist.data.repo.BaseItemsRepository
import com.example.util.simpleshoppinglist.data.repo.ItemsRepository
import com.example.util.simpleshoppinglist.util.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(application: App) {

    private var appContext: Context = application.applicationContext

    @Provides
    @Singleton
    @Named("AppContext")
    fun getAppContext(): Context {
        return appContext
    }

    @Provides
    @Singleton
    fun getAppDatabase(@Named("AppContext") context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun getAppExecutors(): AppExecutors {
        return AppExecutors()
    }

    @Provides
    @Singleton
    fun getItemDao(database: AppDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    @Singleton
    fun getBaseItemsRepository(executors: AppExecutors, dao: ItemDao): BaseItemsRepository {
        return ItemsRepository.getInstance(executors, dao)
    }

}