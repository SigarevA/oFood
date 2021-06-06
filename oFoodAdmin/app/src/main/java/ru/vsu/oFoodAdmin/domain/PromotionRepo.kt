package ru.vsu.oFoodAdmin.domain

import ru.vsu.oFoodAdmin.network.requests.CreationPromotionDTO
import ru.vsu.oFoodAdmin.network.response.DetailPromotionResponse
import ru.vsu.oFoodAdmin.network.response.PromotionResponse
import ru.vsu.oFoodAdmin.network.response.StatusResponse

interface PromotionRepo {
    suspend fun createPromotion(creationPromotionDTO: CreationPromotionDTO) : Result<StatusResponse>
    suspend fun getAllPromotions(): Result<List<PromotionResponse>>
    suspend fun getDetailPromotions(id: String): Result<DetailPromotionResponse>
}