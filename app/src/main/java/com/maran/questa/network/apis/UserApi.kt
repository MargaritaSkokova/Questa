package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.UUID

interface UserApi : IApi<User> {
    @GET("theme/all")
    override suspend fun getAll(): List<User>

    @GET("theme/{id}")
    override suspend fun getById(id: UUID): User?

    @GET("theme/name/{name}")
    suspend fun getByName(name: String): List<User>

    @POST("theme")
    override suspend fun insert(value: User): User?

    @PUT("theme")
    override suspend fun update(value: User): User?

    @DELETE("theme/{id}")
    override suspend fun delete(id: UUID)
}