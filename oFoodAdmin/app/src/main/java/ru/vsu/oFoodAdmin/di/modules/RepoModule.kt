package ru.vsu.oFoodAdmin.di.modules

import dagger.Binds
import dagger.Module
import ru.vsu.oFoodAdmin.domain.*
import javax.inject.Singleton

@Module
abstract class RepoModule {
    @Singleton
    @Binds
    abstract fun bindDishRepo(dishRepoIml: DishRepoImpl) : DishRepo

    @Singleton
    @Binds
    abstract fun bindOrderRepo(orderRepoIml: OrderRepoIml) : OrderRepo

    @Singleton
    @Binds
    abstract fun bindPromotionRepo(promotionRepoImpl: PromotionRepoImpl) : PromotionRepo

    @Singleton
    @Binds
    abstract fun bindCategoryRepo(categoryRepoImpl: CategoryRepoImpl) : CategoryRepo
}