package com.maran.questa.network.apis

import java.util.UUID

interface IApi<T> {
    suspend fun getAll(): List<T>
    suspend fun getById(id: UUID): T?
    suspend fun insert(value: T): T?
    suspend fun update(value: T): T?
    suspend fun delete(id: UUID)
}