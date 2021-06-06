package ru.vsu.ofood.di.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.vsu.ofood.network.api.*
import ru.vsu.ofood.network.request.OrderCreationRequest
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideDishApi(retrofit: Retrofit): DishApi =
        retrofit.create(DishApi::class.java)

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit: Retrofit) : CategoryAPI =
        retrofit.create(CategoryAPI::class.java)

    @Provides
    @Singleton
    fun provideRestaurantAPI(retrofit: Retrofit) : RestaurantAPI =
        retrofit.create(RestaurantAPI::class.java)

    @Provides
    @Singleton
    fun provideNotificationAPI(retrofit: Retrofit) : NotificationAPI =
        retrofit.create(NotificationAPI::class.java)

    @Provides
    @Singleton
    fun provideOrderAPI(retrofit: Retrofit) : OrderAPI =
        retrofit.create(OrderAPI::class.java)
}