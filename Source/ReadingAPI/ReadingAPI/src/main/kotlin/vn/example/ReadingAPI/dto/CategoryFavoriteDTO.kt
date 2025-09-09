package vn.example.ReadingAPI.dto

data class CategoryFavoriteDTO(
    val categoryId: Int,
    val categoryName: String,
    val totalFavorites: Int
)
