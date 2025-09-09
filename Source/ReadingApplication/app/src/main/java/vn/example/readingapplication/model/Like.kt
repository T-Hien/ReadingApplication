package vn.example.readingapplication.model

data class Like(
    val id :LikeId?,
    val abook: Book?,
    val auser: User?,
    val createdAt: String? = null,    )
