package vn.example.readingapplication.model

data class Author(
    val id: Int?,
    val name: String?=null,
    val birth_date: String?=null,
    val death_date: String?=null,
    val listDetailAuthor: List<DetailAuthor?>?=null
)
