package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.UUID

interface ResultApi : IApi<Result> {
    @GET("result/all")
    override suspend fun getAll(): List<Result>

    @GET("result/{id}")
    override suspend fun getById(id: UUID): Result?

    @GET("result/test")
    suspend fun getByTest(test: Test): List<Result>

    @POST("result")
    override suspend fun insert(value: Result): Result?

    @PUT("result")
    override suspend fun update(value: Result): Result?

    @DELETE("result/{id}")
    override suspend fun delete(id: UUID)
}