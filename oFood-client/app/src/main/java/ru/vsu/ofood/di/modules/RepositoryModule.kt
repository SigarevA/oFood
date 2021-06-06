package ru.vsu.ofood.di.modules

import dagger.Binds
import dagger.Module
import ru.vsu.ofood.domain.*

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindCategoryRepo(categoryRepoImpl: CategoryRepoImpl): CategoryRepo

    @Binds
    abstract fun bindDishRepo(dishRepoImpl: DishRepoImpl) : DishRepo

    @Binds
    abstract fun bindBasketRepo(basketRepoImpl : BasketRepoImpl) : BasketRepo

    @Binds
    abstract fun bindRestaurantRepo(restaurantRepoImpl: RestaurantRepoImpl) : RestaurantRepo

    @Binds
    abstract fun bindClientRepo(clientRepoIml: ClientRepoIml) : ClientRepo
}