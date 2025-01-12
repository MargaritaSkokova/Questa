package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.Body
import retrofit2.http.POST

interface SecurityApi {
    @POST("/login")
    suspend fun login(@Body authentication: Authentication): String
}