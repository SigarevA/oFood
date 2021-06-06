package ru.vsu.oFoodAdmin.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.vsu.oFoodAdmin.di.modules.ApiModule
import ru.vsu.oFoodAdmin.di.modules.NetworkModule
import ru.vsu.oFoodAdmin.di.modules.RepoModule
import ru.vsu.oFoodAdmin.domain.CategoryRepo
import ru.vsu.oFoodAdmin.domain.DishRepo
import ru.vsu.oFoodAdmin.domain.OrderRepo
import ru.vsu.oFoodAdmin.domain.PromotionRepo
import javax.inject.Singleton

@Component(
    modules = [
        NetworkModule::class,
        RepoModule::class,
        ApiModule::class
    ]
)
@Singleton
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun getDishRepo() : DishRepo
    fun getOrderRepo() : OrderRepo
    fun getPromotionRepo() : PromotionRepo
    fun getCategoryRepo() : CategoryRepo
}