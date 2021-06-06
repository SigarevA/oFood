package ru.vsu.ofood.domain

import android.provider.Settings.Global.getString
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.vsu.ofood.R
import ru.vsu.ofood.db.DishInBasketDao
import ru.vsu.ofood.entities.Dish
import ru.vsu.ofood.entities.DishInBasket
import ru.vsu.ofood.network.api.OrderAPI
import ru.vsu.ofood.network.request.OrderCreationRequest
import ru.vsu.ofood.network.responses.StatusResponse
import ru.vsu.ofood.ui.order_registration.OrderRegistrationState
import ru.vsu.ofood.utils.toListInfoDishInOrderRequest
import ru.vsu.ofood.utils.toOrderCreationRequest
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = "BasketRepoImpl"

class BasketRepoImpl @Inject constructor(
    private val basketDao: DishInBasketDao,
    private val orderAPI: OrderAPI
) : BasketRepo {
    override suspend fun addDishToBasket(dish: Dish, count: Int) {
        Log.d("BasketRepoImpl", "add dish to basket")
        val dishInBasket = basketDao.getDishFromBasket(dish.id)
        dishInBasket?.let {
            basketDao.updateDishInBasket(it.copy(count = it.count + count))
        } ?: basketDao.insertDishInBasket(DishInBasket(count = count, dishId = dish.id))
    }

    override suspend fun getDishes(): List<DishInBasket> {
        return basketDao.getDishesFromBasket()
    }

    override suspend fun deleteDishFromBasket(dish: Dish): Boolean {
        Log.d(TAG, "delete ${dish} from basket")
        val result = basketDao.deleteDishInBasketWithDishId(dish.id)
        Log.d(TAG, "result op : ${result}")
        return result == 1
    }

    override suspend fun changeCount(dish: Dish, flag: Int): Int {
        val dishInBasket = basketDao.getDishFromBasket(dishId = dish.id)
        return dishInBasket?.let {
            if (flag == -1 && dishInBasket.count == 1)
                return 1
            val addValue = flag * 1
            basketDao.updateDishInBasket(it.copy(count = it.count + addValue))
            it.count + addValue
        } ?: -1
    }

    override suspend fun clearBasket() {
        basketDao.clearBasket()
    }

    @ExperimentalCoroutinesApi
    override suspend fun createOrder(orderRegistrationState: OrderRegistrationState): StatusResponse {
        val token = suspendCancellableCoroutine<String?> { cont ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    cont.resumeWithException(task.exception as Throwable)
                    return@OnCompleteListener
                }
                val token = task.result
                cont.resume(token)
            }
            )
        }
        val dishes = getDishes()
        val requestBody = orderRegistrationState.toOrderCreationRequest(
            token!!,
            dishes.toListInfoDishInOrderRequest()
        )
        return orderAPI.createOrder(requestBody)
    }
}