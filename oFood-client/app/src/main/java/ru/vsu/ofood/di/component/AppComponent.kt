package ru.vsu.ofood.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.vsu.ofood.di.modules.*
import ru.vsu.ofood.domain.CategoryRepo
import ru.vsu.ofood.notification.MyFirebaseMessagingService
import ru.vsu.ofood.ui.MainActivity
import javax.inject.Singleton

@Component(
    modules = [
        NetworkModule::class,
        ApiModule::class,
        NavigationModule::class,
        FactoryModule::class,
        RepositoryModule::class,
        DataBaseModule::class
    ]
)
@Singleton
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun inject(mainActivity: MainActivity)
    fun inject(myFirebaseMessagingService: MyFirebaseMessagingService)

    fun plusHomeModule(HomeModule : HomeModule) : HomeComponent
    fun plusBasketModule(basketModule: BasketModule) : BasketComponent
    fun plusInfoComponent(infoModule: InfoModule) : InfoComponent
}