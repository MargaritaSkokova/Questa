package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.UUID

interface RoleApi : IApi<Role> {
    @GET("role/all")
    override suspend fun getAll(): List<Role>

    @GET("role/{id}")
    override suspend fun getById(id: UUID): Role?

    @GET("role/name/{name}")
    suspend fun getByName(name: String): List<Role>

    @POST("role")
    override suspend fun insert(value: Role): Role?

    @PUT("role")
    override suspend fun update(value: Role): Role?

    @DELETE("role/{id}")
    override suspend fun delete(id: UUID)
}