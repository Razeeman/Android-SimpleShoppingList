package com.example.util.simpleshoppinglist

interface BasePresenter<T> {

    fun attachView(view: T)
    fun detachView()

}