package vn.example.readingapplication.model

data class Notification(
    val id: Int?,
    val abook: Book?,
    val message: String?,
    val type: String?,
    val createdAt: String?
)
