package ru.vsu.ofood.ui.basket

import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.vsu.ofood.domain.BasketRepo
import ru.vsu.ofood.domain.DishRepo
import ru.vsu.ofood.dto.DishToBasketDTO
import ru.vsu.ofood.entities.Dish
import ru.vsu.ofood.ui.Interpret

private const val TAG = "BasketScreenMiddleware"

class BasketScreenMiddleware(
    private val basketRepo: BasketRepo,
    private val dishRepo: DishRepo
) : Interpret<BasketAction> {
    private val mutableBasketState = MutableStateFlow<BasketState>(BasketState.EmptyState)
    val basketState: StateFlow<BasketState>
        get() = mutableBasketState.asStateFlow()

    private val middlewareScope = MainScope()

    init {
        complete(BasketAction.LoadDishes)
    }

    fun destroy() {
        middlewareScope.cancel()
    }

    override fun complete(action: BasketAction) {
        Log.d(TAG, "action : ${action}")
        when (action) {
            is BasketAction.LoadDishes -> {
                processLoadDishes()
            }
            is BasketAction.DeleteDishFromBasketAction -> {
                processDeleteDishFromBasket(action.dish)
            }
            is BasketAction.IncreaseCountEvent -> {
                changeCountDishInBasket(action.dish, 1)
            }
            is BasketAction.DecreaseCountEvent -> {
                changeCountDishInBasket(action.dish, -1)
            }
            is BasketAction.PlaceAnOrderAction -> {
            }
        }
    }

    private fun changeCountDishInBasket(dish: Dish, flag: Int) {
        middlewareScope.launch {
            val currentCount = basketRepo.changeCount(dish, flag)
            if (currentCount != -1 && basketState.value is BasketState.ContentDishesState) {
                val dishesInBasketDTO = (basketState.value as BasketState.ContentDishesState).dishes
                mutableBasketState.value = BasketState.ContentDishesState(
                    dishes = dishesInBasketDTO.map {
                        if (it.dish.id == dish.id) DishToBasketDTO(
                            dish,
                            currentCount
                        ) else it
                    }
                )
            }
        }
    }

    private fun processLoadDishes() {
        Log.d(TAG, "load dishes in basket")
        try {
            middlewareScope.launch {
                try {
                    val dishesInBasket = basketRepo.getDishes()
                    val dishInBasketMap = HashMap<String, Int>()
                    dishesInBasket.forEach { dishInBasketMap[it.dishId] = it.count }
                    Log.d(TAG, "count dish in basket : ${dishesInBasket.size}")
                    dishesInBasket.forEach { dish ->
                        Log.d(
                            TAG,
                            "item : {${dish.dishId}}, count : ${dish.count}"
                        )
                    }
                    val dishes = dishRepo.getDishes("607af73f04cf13eb9a56aa78")
                    val dishesInBasketDTO = ArrayList<DishToBasketDTO>()
                    dishes.forEach {
                        if (dishInBasketMap.containsKey(it.id)) {
                            dishesInBasketDTO.add(
                                DishToBasketDTO(
                                    it,
                                    dishInBasketMap[it.id]!!
                                )
                            )
                        }
                    }
                    mutableBasketState.value = if (dishesInBasketDTO.isEmpty())
                        BasketState.EmptyDishesState
                    else
                        BasketState.ContentDishesState(dishesInBasketDTO)
                } catch (ex: Exception) {
                    Log.e(TAG, "err", ex)
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "err", ex)
        }
    }

    private fun processDeleteDishFromBasket(dish: Dish) {
        middlewareScope.launch {
            val result = basketRepo.deleteDishFromBasket(dish)
            if (basketState.value is BasketState.ContentDishesState && result) {
                val tempDishes = (basketState.value as BasketState.ContentDishesState).dishes
                    .filter { it.dish.id != dish.id }
                mutableBasketState.value = if (tempDishes.isEmpty())
                    BasketState.EmptyDishesState
                else
                    BasketState.ContentDishesState(tempDishes)
            }
        }
    }
}