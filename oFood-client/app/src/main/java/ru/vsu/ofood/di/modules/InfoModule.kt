package ru.vsu.ofood.di.modules

import dagger.Module
import dagger.Provides
import ru.vsu.ofood.di.scopes.InfoTabScope
import ru.vsu.ofood.domain.RestaurantRepo
import ru.vsu.ofood.ui.Event
import ru.vsu.ofood.ui.info.InfoMiddleware

@Module
class InfoModule(private val processEvent : (Event) -> Unit) {
    @Provides
    @InfoTabScope
    fun getInfoMiddleware(
        restaurantRepo: RestaurantRepo
    ) : InfoMiddleware =
        InfoMiddleware(
            restaurantRepo,
            processEvent
        )
}