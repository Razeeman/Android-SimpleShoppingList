package com.example.util.simpleshoppinglist.ui.additem

import com.example.util.simpleshoppinglist.di.ActivityScoped
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(modules = [AddItemModule::class])
interface AddItemComponent {

    fun inject(fragment: AddItemDialogFragment)

}