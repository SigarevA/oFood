package ru.vsu.oFoodAdmin

import android.app.Application
import ru.vsu.oFoodAdmin.di.ComponentHolder
import ru.vsu.oFoodAdmin.di.component.DaggerAppComponent

class FoodAdminApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        ComponentHolder.appComponent = DaggerAppComponent.factory().create(this)
    }
}