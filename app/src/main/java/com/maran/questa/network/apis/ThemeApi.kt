package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface ThemeApi {
    @GET("theme/all")
    suspend fun getAll(): List<Theme>

    @GET("theme/{id}")
    suspend fun getById(@Path("id") id: UUID): Theme?

    @GET("theme/name/{name}")
    fun getByName(@Path("name") name: String): List<Theme>

    @POST("theme")
    suspend fun insert(@Body value: Theme): Theme?

    @PUT("theme")
    suspend fun update(@Body value: Theme): Theme?

    @DELETE("theme/{id}")
    suspend fun delete(@Path("id") id: UUID)
}