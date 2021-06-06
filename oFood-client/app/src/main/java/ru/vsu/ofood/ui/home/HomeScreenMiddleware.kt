package ru.vsu.ofood.ui.home

import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.ofood.domain.CategoryRepo
import ru.vsu.ofood.domain.DishRepo
import ru.vsu.ofood.entities.Category
import ru.vsu.ofood.ui.*

private const val TAG = "HomeScreenMiddleware"

class HomeScreenMiddleware(
    private val categoryRepo: CategoryRepo,
    private val dishRepo: DishRepo,
    private val navigate : (Destination) -> Unit
) : Interpret<HomeScreenEvent> {
    val mutableState: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState.Empty)
    val state
        get() = mutableState.asStateFlow()

    val middlewareScope = MainScope()

    init {
        complete(HomeScreenEvent.InitEvent)
    }

    private fun handleLoadingDishesByCategoryEvent(category : Category) {
        mutableState.value = (mutableState.value as HomeScreenState.ContentCategories)
            .copy(dishState = DishState.LoadingDishes)
        middlewareScope.launch {
            try {
                val dishes = dishRepo.getDishes(category.id)
                mutableState.value
                    .takeIf { it is HomeScreenState.ContentCategories }
                    .let {
                        mutableState.value = ( mutableState.value as HomeScreenState.ContentCategories)
                            .copy(dishState = DishState.ContentDishes(dishes))
                    }
            }
            catch (ex : Exception) {
                Log.d(TAG, "Exception!")
                Log.e(TAG, "Ex", ex)
            }
        }
    }

    private fun handleInitEvent(event: HomeScreenEvent.InitEvent) {
        mutableState.value = HomeScreenState.LoadingCategories
        middlewareScope.launch {
            try {
                val categories = categoryRepo.getCategories()
                if (categories.size == 0) {
                    mutableState.value =
                        HomeScreenState.EmptyCategories
                }
                else {
                    mutableState.value =
                        HomeScreenState.ContentCategories(
                            categories,
                            categories[0]
                        )
                    complete(HomeScreenEvent.LoadingDishesByCategoryEvent(categories[0]))
                }
            } catch (ex: Exception) {
                Log.d(TAG, "Exception")
            }
        }
    }

    fun destroy() {
        middlewareScope.cancel()
    }

    override fun complete(action: HomeScreenEvent) {
        when (action) {
            is HomeScreenEvent.InitEvent -> {
                handleInitEvent(action)
            }
            is HomeScreenEvent.LoadingDishesByCategoryEvent -> {
                handleLoadingDishesByCategoryEvent(action.category)
            }
            is HomeScreenEvent.ClickDetailDishEvent -> {
                navigate(Destination.DetailDish(action.dish))
            }
        }
    }
}