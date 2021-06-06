package ru.vsu.oFoodAdmin.di.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.vsu.oFoodAdmin.network.api.CategoryAPI
import ru.vsu.oFoodAdmin.network.api.DishAPI
import ru.vsu.oFoodAdmin.network.api.OrderAPI
import ru.vsu.oFoodAdmin.network.api.PromotionAPI
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideDishAPI(retrofit : Retrofit) : DishAPI =
        retrofit.create(DishAPI::class.java)

    @Provides
    @Singleton
    fun provideOrderAPI(retrofit: Retrofit) : OrderAPI =
        retrofit.create(OrderAPI::class.java)

    @Provides
    @Singleton
    fun providePromotionAPI(retrofit: Retrofit) : PromotionAPI =
        retrofit.create(PromotionAPI::class.java)

    @Provides
    @Singleton
    fun provideCategoryAPI(retrofit: Retrofit) : CategoryAPI =
        retrofit.create(CategoryAPI::class.java)
}