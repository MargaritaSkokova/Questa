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
    suspend fun getAll(): List<Test>

    @GET("test/{id}")
    suspend fun getById(@Path("id") id: UUID): Test?

    @GET("test/theme")
    suspend fun getByTheme(@Body theme: Theme): List<Test>

    @GET("test/author")
    suspend fun getByAuthor(@Body author: User): List<Test>

    @GET("test/type/{type}")
    suspend fun getByType(@Path("type") type: String): List<Test>

    @POST("test")
    suspend fun insert(@Body value: Test): Test?

    @PUT("test")
    suspend fun update(@Body value: Test): Test?

    @DELETE("test/{id}")
    suspend fun delete(@Path("id") id: UUID)
}