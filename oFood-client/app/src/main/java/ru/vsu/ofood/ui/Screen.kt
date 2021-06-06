package ru.vsu.ofood.ui

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.github.terrakok.modo.NavigationAction
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.ofood.R
import ru.vsu.ofood.entities.Category
import ru.vsu.ofood.entities.Coordinate
import ru.vsu.ofood.entities.Dish
import ru.vsu.ofood.ui.home.HomeScreenMiddleware

object EmptyState

sealed class Tab(
    @StringRes val label: Int,
    @DrawableRes val icon: Int
) {
    object Home : Tab(R.string.tab_home, R.drawable.ic_home)
    object Basket : Tab(R.string.tab_basket, R.drawable.ic_basket)
    object Info : Tab(R.string.tab_info, R.drawable.ic_info)
}

val tabs = listOf(Tab.Home, Tab.Info, Tab.Basket)

sealed class AppState {
    object HomeScreen : AppState()
    class DishDetail(
        val dish: Dish
    ) : AppState()
}

sealed class HomeScreenEvent {
    object InitEvent : HomeScreenEvent()
    class LoadingDishesByCategoryEvent(val category: Category) : HomeScreenEvent()
    class ClickDetailDishEvent(val dish: Dish) : HomeScreenEvent()
}

sealed class HomeScreenState {
    object Empty : HomeScreenState()
    object LoadingCategories : HomeScreenState()
    object EmptyCategories : HomeScreenState()
    class ErrorCategories(ex: Throwable)
    data class ContentCategories(
        val categories: List<Category>,
        val selectedCategory: Category,
        val dishState: DishState = DishState.Empty
    ) :
        HomeScreenState()
}

sealed class DishState {
    object Empty : DishState()
    object LoadingDishes : DishState()
    object EmptyDishes : DishState()
    class ContentDishes(val dishes: List<Dish>) : DishState()
}

sealed class NavigationOperation {
    object Back : NavigationOperation()
    class Destination (val screen: Screen) : NavigationOperation()
}

sealed class BasketDestination {
    object Basket : BasketDestination(), Screen
    object OrderRegistration : BasketDestination(), Screen
}

sealed class InfoDestination {
    object Contacts : InfoDestination(), Screen
    class Localization(val coordinate: Coordinate) : InfoDestination(), Screen
}

sealed class Event {
    class NavigateToActivityCallNumber(val phoneNumber : String ) : Event()
}

interface Screen