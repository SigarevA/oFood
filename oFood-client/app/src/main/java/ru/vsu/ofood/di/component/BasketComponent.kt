package ru.vsu.ofood.di.component

import dagger.Subcomponent
import ru.vsu.ofood.di.modules.BasketModule
import ru.vsu.ofood.di.scopes.BasketTabScope
import ru.vsu.ofood.ui.basket.BasketScreenMiddleware
import ru.vsu.ofood.ui.order_registration.OrderRegistrationMiddleware

@Subcomponent(modules = [BasketModule::class])
@BasketTabScope
interface BasketComponent {
    fun getBasketScreenMiddleware() : BasketScreenMiddleware
    fun getOrderRegistrationMiddleware() : OrderRegistrationMiddleware
}