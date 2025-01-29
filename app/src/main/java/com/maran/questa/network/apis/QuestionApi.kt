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
    suspend fun getAll(): kotlin.Result<List<Question>>

    @GET("question/{id}")
    suspend fun getById(@Path("id") id: UUID): kotlin.Result<Question?>

    @GET("question/test/{id}")
    suspend fun getByTest(@Path("id") id: UUID): kotlin.Result<List<Question>>

    @POST("question")
    suspend fun insert(@Body value: Question): kotlin.Result<Question?>

    @PUT("question")
    suspend fun update(@Body value: Question): kotlin.Result<Question?>

    @DELETE("question/{id}")
    suspend fun delete(@Path("id") id: UUID): kotlin.Result<Any>
}