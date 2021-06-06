package ru.vsu.ofood.di

import ru.vsu.ofood.di.component.AppComponent
import ru.vsu.ofood.di.component.BasketComponent
import ru.vsu.ofood.di.component.HomeComponent
import ru.vsu.ofood.di.component.InfoComponent
import ru.vsu.ofood.di.scopes.HomeTabScope

object ComponentHolder {
    lateinit var appComponent: AppComponent

    var homeTabComponent : HomeComponent? = null
    var basketTabComponent : BasketComponent? = null
    var infoComponent : InfoComponent? = null
}