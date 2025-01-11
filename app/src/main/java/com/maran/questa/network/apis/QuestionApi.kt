package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.UUID

interface QuestionApi : IApi<Question> {
    @GET("question/all")
    override suspend fun getAll(): List<Question>

    @GET("question/{id}")
    override suspend fun getById(id: UUID): Question?

    @GET("question/test")
    suspend fun getByTest(test: Test): List<Question>

    @POST("question")
    override suspend fun insert(value: Question): Question?

    @PUT("question")
    override suspend fun update(value: Question): Question?

    @DELETE("question/{id}")
    override suspend fun delete(id: UUID)
}