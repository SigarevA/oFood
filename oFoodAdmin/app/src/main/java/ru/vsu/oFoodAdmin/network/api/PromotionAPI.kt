package ru.vsu.oFoodAdmin.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.vsu.oFoodAdmin.network.requests.CreationPromotionDTO
import ru.vsu.oFoodAdmin.network.response.DetailOrderResponse
import ru.vsu.oFoodAdmin.network.response.DetailPromotionResponse
import ru.vsu.oFoodAdmin.network.response.PromotionResponse
import ru.vsu.oFoodAdmin.network.response.StatusResponse

interface PromotionAPI {

    @GET("/promotion/all")
    suspend fun getPromotions(): List<PromotionResponse>

    @GET("/detail-promotion/{id}")
    suspend fun getDetailPromotion(@Path("id") id: String): DetailPromotionResponse

    @POST("/promotion/create")
    suspend fun createPromotion(@Body creationPromotionDTO: CreationPromotionDTO) : StatusResponse
}