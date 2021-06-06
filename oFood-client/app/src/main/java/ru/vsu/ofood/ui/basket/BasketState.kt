package ru.vsu.ofood.ui.basket

import androidx.compose.runtime.Composable
import ru.vsu.ofood.dto.DishToBasketDTO
import ru.vsu.ofood.entities.Dish

sealed class BasketState {
    object EmptyState : BasketState()
    object EmptyDishesState : BasketState()
    class ContentDishesState(val dishes : List<DishToBasketDTO>) : BasketState()
    class ErrorState(val msg : String) : BasketState()
}

sealed class BasketAction {
    class DeleteDishFromBasketAction(val dish : Dish) : BasketAction()
    class IncreaseCountEvent(val dish : Dish) : BasketAction()
    class DecreaseCountEvent(val dish : Dish) : BasketAction()
    object PlaceAnOrderAction : BasketAction()
    object LoadDishes : BasketAction()
}