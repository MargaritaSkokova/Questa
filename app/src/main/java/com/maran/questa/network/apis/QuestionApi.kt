package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface QuestionApi {
    @GET("question/all")
    suspend fun getAll(): List<Question>

    @GET("question/{id}")
    suspend fun getById(@Path("id") id: UUID): Question?

    @GET("question/test")
    suspend fun getByTest(@Body test: Test): List<Question>

    @POST("question")
    suspend fun insert(@Body value: Question): Question?

    @PUT("question")
    suspend fun update(@Body value: Question): Question?

    @DELETE("question/{id}")
    suspend fun delete(@Path("id") id: UUID)
}