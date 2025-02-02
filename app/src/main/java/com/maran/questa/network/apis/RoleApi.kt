package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface RoleApi {
    @GET("role/all")
    suspend fun getAll(): kotlin.Result<List<Role>>

    @GET("role/{id}")
    suspend fun getById(@Path("id") id: UUID): kotlin.Result<Role?>

    @GET("role/name/{name}")
    suspend fun getByName(@Path("name") name: String): kotlin.Result<List<Role>>

    @POST("role")
    suspend fun insert(@Body value: Role): kotlin.Result<Role?>

    @PUT("role")
    suspend fun update(@Body value: Role): kotlin.Result<Role?>

    @DELETE("role/{id}")
    suspend fun delete(@Path("id") id: UUID): kotlin.Result<String?>
}