package ru.vsu.ofood.di.modules

import dagger.Module
import dagger.Provides
import ru.vsu.ofood.di.scopes.HomeTabScope
import ru.vsu.ofood.domain.BasketRepo
import ru.vsu.ofood.domain.CategoryRepo
import ru.vsu.ofood.domain.DishRepo
import ru.vsu.ofood.ui.Destination
import ru.vsu.ofood.ui.detail.DishScreenMiddleware
import ru.vsu.ofood.ui.home.HomeScreenMiddleware

@Module
class HomeModule(
    private val navigate: (Destination) -> Unit
) {
    @Provides
    @HomeTabScope
    fun provideHomeScreenMiddleware(
        categoryRepo: CategoryRepo,
        dishRepo: DishRepo,
    ): HomeScreenMiddleware {
        return HomeScreenMiddleware(
            categoryRepo,
            dishRepo,
            navigate
        )
    }

    @Provides
    @HomeTabScope
    fun provideDetailDish(basketRepo: BasketRepo) : DishScreenMiddleware {
        return DishScreenMiddleware(basketRepo)
    }
}