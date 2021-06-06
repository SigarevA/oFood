package ru.vsu.oFoodAdmin.domain

import ru.vsu.oFoodAdmin.network.api.PromotionAPI
import ru.vsu.oFoodAdmin.network.requests.CreationPromotionDTO
import ru.vsu.oFoodAdmin.network.response.DetailPromotionResponse
import ru.vsu.oFoodAdmin.network.response.PromotionResponse
import ru.vsu.oFoodAdmin.network.response.StatusResponse
import javax.inject.Inject

class PromotionRepoImpl @Inject constructor(private val promotionAPI: PromotionAPI) :
    PromotionRepo {
    override suspend fun createPromotion(creationPromotionDTO: CreationPromotionDTO): Result<StatusResponse> {
        return try {
            Result.Success(promotionAPI.createPromotion(creationPromotionDTO))
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    override suspend fun getAllPromotions(): Result<List<PromotionResponse>> {
        return try {
            val detail = promotionAPI.getPromotions()
            Result.Success(detail)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }

    override suspend fun getDetailPromotions(id: String): Result<DetailPromotionResponse> {
        return try {
            val detail = promotionAPI.getDetailPromotion(id)
            Result.Success(detail)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}