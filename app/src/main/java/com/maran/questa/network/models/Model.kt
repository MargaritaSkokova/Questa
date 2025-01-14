package com.maran.questa.network.models

import com.maran.questa.network.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed class Model {
    @Serializable
    data class Answer(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        val question: Question,
        val text: String,
        val isCorrect: Boolean?,
        val personality: String?
    ) : Model()

    @Serializable
    data class Question(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        val text: String,
        val test: Test,
        val order: Int,
    ) : Model()

    @Serializable
    data class Result(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        val test: Test,
        val resultMessage: String,
        val maxPoints: Int?,
        val personality: String?
    ) : Model()

    @Serializable
    data class Role(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        val name: String
    ) : Model()

    @Serializable
    data class Test(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        val type: String,
        val name: String,
        val author: User,
        val theme: Theme,
        val description: String,
    ) : Model()

    @Serializable
    data class Theme(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        val name: String
    ) : Model()

    @Serializable
    data class User(
        @Serializable(with = UUIDSerializer::class)
        val id: UUID,
        val username: String,
        val role: Role,
        val password: String
    ) : Model()

    @Serializable
    data class Authentication(
        val username: String,
        val password: String
    ) : Model()
}