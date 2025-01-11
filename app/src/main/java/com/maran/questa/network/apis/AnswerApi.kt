package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.UUID

interface AnswerApi : IApi<Answer> {
    @GET("answer/all")
    override suspend fun getAll(): List<Answer>

    @GET("answer/{id}")
    override suspend fun getById(id: UUID): Answer?

    @GET("answer/question")
    suspend fun getByQuestion(question: Question): List<Answer>

    @POST("answer")
    override suspend fun insert(value: Answer): Answer?

    @PUT("answer")
    override suspend fun update(value: Answer): Answer?

    @DELETE("answer/{id}")
    override suspend fun delete(id: UUID)
}