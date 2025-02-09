package com.makvas.bookshelfapp

import android.app.Application
import com.makvas.bookshelfapp.data.AppContainer
import com.makvas.bookshelfapp.data.DefaultAppContainer

class BookshelfApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}