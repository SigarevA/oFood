package ru.vsu.ofood.di.component

import dagger.Subcomponent
import ru.vsu.ofood.di.modules.HomeModule
import ru.vsu.ofood.di.scopes.HomeTabScope
import ru.vsu.ofood.ui.detail.DishScreenMiddleware
import ru.vsu.ofood.ui.home.HomeScreenMiddleware

@Subcomponent(modules = [HomeModule::class])
@HomeTabScope
interface HomeComponent {
    fun getHomeScreenMiddleware() : HomeScreenMiddleware
    fun getDishScreenMiddleware() : DishScreenMiddleware
}