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
    suspend fun getAll(): List<Result>

    @GET("result/{id}")
    suspend fun getById(@Path("id") id: UUID): Result?

    @GET("result/test")
    suspend fun getByTest(@Body test: Test): List<Result>

    @POST("result")
    suspend fun insert(@Body value: Result): Result?

    @PUT("result")
    suspend fun update(@Body value: Result): Result?

    @DELETE("result/{id}")
    suspend fun delete(@Path("id")  id: UUID)
}