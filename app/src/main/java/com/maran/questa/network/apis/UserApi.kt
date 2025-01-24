package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface UserApi {
    @GET("user/all")
    suspend fun getAll(): kotlin.Result<List<User>>

    @GET("user/{id}")
    suspend fun getById(@Path("id") id: UUID): kotlin.Result<User?>

    @GET("user/name/{name}")
    suspend fun getByName(@Path("name") name: String): kotlin.Result<List<User>>

    @POST("user")
    suspend fun insert(@Body value: User): kotlin.Result<User?>

    @PUT("user")
    suspend fun update(@Body value: User): kotlin.Result<User?>

    @DELETE("user/{id}")
    suspend fun delete(@Path("id") id: UUID): kotlin.Result<Any>
}