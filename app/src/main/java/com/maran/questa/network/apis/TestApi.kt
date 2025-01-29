package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface TestApi {
    @GET("test/all")
    suspend fun getAll(): kotlin.Result<List<Test>>

    @GET("test/{id}")
    suspend fun getById(@Path("id") id: UUID): kotlin.Result<Test?>

    @GET("test/theme/{name}")
    suspend fun getByTheme(@Path("name") type: String): kotlin.Result<List<Test>>

    @GET("test/author/{name}")
    suspend fun getByAuthor(@Path("name") type: String): kotlin.Result<List<Test>>

    @GET("test/type/{type}")
    suspend fun getByType(@Path("type") type: String): kotlin.Result<List<Test>>

    @POST("test")
    suspend fun insert(@Body value: Test): kotlin.Result<Test?>

    @PUT("test")
    suspend fun update(@Body value: Test): kotlin.Result<Test?>

    @DELETE("test/{id}")
    suspend fun delete(@Path("id") id: UUID): kotlin.Result<Any>
}