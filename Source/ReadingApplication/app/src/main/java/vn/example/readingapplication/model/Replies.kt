package vn.example.readingapplication.model

data class Replies(
    val id: Int?,
    val note: Int?,
    val auser: User?,
    val content: String,
    val createdAt: String?
)
