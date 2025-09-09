package vn.example.readingapplication.model

data class Search(
    val id: Int?,
    val auser: User?,
    val type: String,
    val keyword: String
)
