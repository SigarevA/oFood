package ru.vsu.ofood.domain

import ru.vsu.ofood.network.responses.StatusResponse

interface ClientRepo {
    suspend fun register(registrationId : String) : StatusResponse
}