package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.UUID

interface ThemeApi : IApi<Theme> {
    @GET("theme/all")
    override suspend fun getAll(): List<Theme>

    @GET("theme/{id}")
    override suspend fun getById(id: UUID): Theme?

    @GET("theme/name/{name}")
    suspend fun getByName(name: String): List<Theme>

    @POST("theme")
    override suspend fun insert(value: Theme): Theme?

    @PUT("theme")
    override suspend fun update(value: Theme): Theme?

    @DELETE("theme/{id}")
    override suspend fun delete(id: UUID)
}