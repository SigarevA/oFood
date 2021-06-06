package ru.vsu.ofood.di.modules

import dagger.Module
import dagger.Provides
import ru.vsu.ofood.di.scopes.BasketTabScope
import ru.vsu.ofood.domain.BasketRepo
import ru.vsu.ofood.domain.DishRepo
import ru.vsu.ofood.ui.NavigationOperation
import ru.vsu.ofood.ui.basket.BasketScreenMiddleware
import ru.vsu.ofood.ui.order_registration.OrderRegistrationMiddleware
import javax.inject.Qualifier

@Module
class BasketModule {
    @Provides
    @BasketTabScope
    fun provideBasketScreenMiddleware(
        basketRepo: BasketRepo,
        dishRepo: DishRepo
    ): BasketScreenMiddleware {
        return BasketScreenMiddleware(basketRepo, dishRepo)
    }

    @Provides
    @BasketTabScope
    fun provideOrderRegistrationMiddleware(basketRepo: BasketRepo): OrderRegistrationMiddleware {
        return OrderRegistrationMiddleware(basketRepo)
    }
}