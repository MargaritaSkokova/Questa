package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface AnswerApi {
    @GET("answer/all")
    suspend fun getAll(): kotlin.Result<List<Answer>>

    @GET("answer/{id}")
    suspend fun getById(@Path("id") id: UUID): kotlin.Result<Answer?>

    @GET("answer/question")
    suspend fun getByQuestion(@Body question: Question): kotlin.Result<List<Answer>>

    @POST("answer")
    suspend fun insert(@Body value: Answer): kotlin.Result<Answer?>

    @PUT("answer")
    suspend fun update(@Body value: Answer): kotlin.Result<Answer?>

    @DELETE("answer/{id}")
    suspend fun delete(@Path("id") id: UUID): kotlin.Result<Any>
}