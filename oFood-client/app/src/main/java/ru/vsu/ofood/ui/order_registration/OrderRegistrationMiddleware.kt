package ru.vsu.ofood.ui.order_registration

import android.util.Log
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.vsu.ofood.domain.BasketRepo
import ru.vsu.ofood.network.responses.StatusResponse
import ru.vsu.ofood.ui.Interpret

private const val TAG = "OrderRegistration"

class OrderRegistrationMiddleware(
    private val basketRepo: BasketRepo
) : Interpret<OrderRegistrationEvent> {

    val middlewareScope = MainScope()

    override fun complete(action: OrderRegistrationEvent) {
        when (action) {
            is OrderRegistrationEvent.ClickCreateOrder -> {
                middlewareScope.launch {
                    val result = basketRepo.createOrder(action.state)
                    Log.d(TAG, "result : $result")
                }
            }
        }
    }

    suspend fun processCreateOrder(event : OrderRegistrationEvent.ClickCreateOrder) : OrderRegistration {
        val dishes = basketRepo.getDishes()
        if (dishes.isEmpty())
            return OrderRegistration.Error("Empty basket")
        val result = basketRepo.createOrder(event.state)
        Log.d(TAG, "$result")
        if (result.status == "success") {
            basketRepo.clearBasket()
            return OrderRegistration.Success
        }
        Log.d(TAG, "result : $result")
        return OrderRegistration.Error(result.message ?: "")
    }
}