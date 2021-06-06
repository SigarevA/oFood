package ru.vsu.ofood.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.vsu.ofood.network.request.RegistrationClientRequest
import ru.vsu.ofood.network.responses.StatusResponse

interface NotificationAPI {
    @POST("/register-client")
    suspend fun registerClient(@Body registrationClientRequest: RegistrationClientRequest): StatusResponse
}