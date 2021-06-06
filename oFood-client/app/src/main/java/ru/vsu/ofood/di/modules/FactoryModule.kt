package ru.vsu.ofood.di.modules

import dagger.Module
import dagger.Provides
import ru.vsu.ofood.domain.CategoryRepo
import ru.vsu.ofood.domain.DishRepo
import ru.vsu.ofood.ui.vms.MainViewModelFactory
import javax.inject.Singleton

@Module
class FactoryModule {
    @Provides
    @Singleton
    fun getViewModelFactory(categoryRepo: CategoryRepo, dishRepo: DishRepo): MainViewModelFactory =
        MainViewModelFactory(categoryRepo, dishRepo)
}