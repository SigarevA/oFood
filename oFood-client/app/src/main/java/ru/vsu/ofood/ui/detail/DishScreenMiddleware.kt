package ru.vsu.ofood.ui.detail

import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import ru.vsu.ofood.domain.BasketRepo
import ru.vsu.ofood.ui.Interpret

class DishScreenMiddleware(
    private val basketRepo: BasketRepo
) : Interpret<DishScreenEvent> {

    private val middlewareScope = MainScope()

    fun destroy() {
        middlewareScope.cancel()
    }

    override fun complete(action: DishScreenEvent) {
        Log.d("DishScreenMiddleware", "action handle")
        when(action) {
            is DishScreenEvent.ClickAddingToBasket -> {
                middlewareScope.launch {
                    basketRepo.addDishToBasket(action.dish, action.count)
                }
            }
        }
    }
}