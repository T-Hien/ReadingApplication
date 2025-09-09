package vn.example.ReadingAPI.dto

data class AuthorFavoriteDTO(
    val id: Int,
    val author_name: String,
    val total_favorites: Int
)

