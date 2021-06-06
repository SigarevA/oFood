package ru.vsu.ofood.domain

import ru.vsu.ofood.network.api.NotificationAPI
import ru.vsu.ofood.network.request.RegistrationClientRequest
import ru.vsu.ofood.network.responses.StatusResponse
import javax.inject.Inject

class ClientRepoIml @Inject constructor(private val notificationAPI: NotificationAPI) : ClientRepo {
    override suspend fun register(registrationId: String) : StatusResponse {
        val registrationClientRequest = RegistrationClientRequest(registrationId)
        return notificationAPI.registerClient(registrationClientRequest)
    }
}