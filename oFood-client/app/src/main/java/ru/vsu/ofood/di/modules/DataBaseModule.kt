package ru.vsu.ofood.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.vsu.ofood.db.BasketDataBase
import ru.vsu.ofood.db.DishInBasketDao
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideMenuDataBase(context: Context) : BasketDataBase =
        BasketDataBase.getInstance(context)

    @Provides
    @Singleton
    fun provideDishInBasketDao(base: BasketDataBase) : DishInBasketDao =
        base.dishInBasketDao()
}