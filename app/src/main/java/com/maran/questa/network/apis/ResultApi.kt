package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface ResultApi {
    @GET("result/all")
    suspend fun getAll(): kotlin.Result<List<Result>>

    @GET("result/{id}")
    suspend fun getById(@Path("id") id: UUID): kotlin.Result<Result?>

    @GET("result/test/{id}")
    suspend fun getByTest(@Path("id") id: UUID): kotlin.Result<List<Result>>

    @POST("result")
    suspend fun insert(@Body value: Result): kotlin.Result<Result?>

    @PUT("result")
    suspend fun update(@Body value: Result): kotlin.Result<Result?>

    @DELETE("result/{id}")
    suspend fun delete(@Path("id")  id: UUID): kotlin.Result<String?>
}