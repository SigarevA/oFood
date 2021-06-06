package ru.vsu.ofood

import android.app.Application
import ru.vsu.ofood.di.ComponentHolder
import ru.vsu.ofood.di.component.DaggerAppComponent

class FoodApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ComponentHolder.appComponent = DaggerAppComponent.factory().create(this)
    }
}