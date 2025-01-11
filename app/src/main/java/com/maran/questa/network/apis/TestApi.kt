package com.maran.questa.network.apis

import com.maran.questa.network.models.Model.*
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.util.UUID

interface TestApi : IApi<Test> {
    @GET("test/all")
    override suspend fun getAll(): List<Test>

    @GET("test/{id}")
    override suspend fun getById(id: UUID): Test?

    @GET("test/theme")
    suspend fun getByTheme(theme: Theme): List<Test>

    @GET("test/author")
    suspend fun getByAuthor(author: User): List<Test>

    @GET("test/type/{type}")
    suspend fun getByType(type: String): List<Test>

    @POST("test")
    override suspend fun insert(value: Test): Test?

    @PUT("test")
    override suspend fun update(value: Test): Test?

    @DELETE("test/{id}")
    override suspend fun delete(id: UUID)
}