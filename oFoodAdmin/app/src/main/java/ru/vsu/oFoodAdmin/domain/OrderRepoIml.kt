package ru.vsu.oFoodAdmin.domain

import android.util.Log
import ru.vsu.oFoodAdmin.entities.Order
import ru.vsu.oFoodAdmin.network.api.OrderAPI
import ru.vsu.oFoodAdmin.network.requests.ManageOrderRequest
import ru.vsu.oFoodAdmin.network.response.DetailOrderResponse
import ru.vsu.oFoodAdmin.network.response.StatusResponse
import javax.inject.Inject

private const val TAG = "OrderRepoIml"

class OrderRepoIml @Inject constructor(private val orderAPI: OrderAPI) : OrderRepo {
    override suspend fun getNewOrder(): Result<List<Order>> {
        return try {
            val orders = orderAPI.getOrdersWithStatus("INIT")
            Result.Success(orders)
        } catch (ex: Exception) {
            Log.e(TAG, "getNewOrder", ex)
            Result.Error(ex)
        }
    }

    override suspend fun getOrderById(id: String): Result<DetailOrderResponse> {
        return try {
            val order = orderAPI.getOrderById(id)
            Result.Success(order)
        } catch (ex: Exception) {
            Log.e(TAG, "getOrderById", ex)
            Result.Error(ex)
        }
    }

    override suspend fun acceptOrder(orderId: String): Result<StatusResponse> {
        return manageOrder(orderId, "ACCEPTED")
    }

    override suspend fun rejectedOrder(orderId: String): Result<StatusResponse> {
        return manageOrder(orderId, "REJECTED")
    }

    private suspend fun manageOrder(orderId: String, status: String): Result<StatusResponse> {
        return try {
            val result = orderAPI.manageOrder(ManageOrderRequest(orderId, status))
            Result.Success(result)
        } catch (ex: Exception) {
            Log.d(TAG, "acceptOrder", ex)
            Result.Error(ex)
        }
    }
}