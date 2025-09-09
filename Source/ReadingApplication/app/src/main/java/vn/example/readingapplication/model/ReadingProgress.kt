package vn.example.readingapplication.model

import java.io.Serializable

data class ReadingProgress(
    val status: String?,
    val progressPercentage: Float?,
    val progressPath: String?,
    val createdAt: String?,
    val auser: User?,
    val abook: Book?,
    val achapter:Chapter?
):Serializable
