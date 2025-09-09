package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.model.BookCategory
import vn.example.ReadingAPI.model.Category
import vn.example.ReadingAPI.model.Search
import vn.example.ReadingAPI.repository.UserRepository

data class CategoryDTO(
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val listBookCategory:List<BookCategory>?
) {
    companion object {
        fun from(category: Category): CategoryDTO {
            return CategoryDTO(
                id = category.id,
                name = category.name,
                description = category.description,
                listBookCategory = emptyList()
            )
        }

        fun CategoryDTO.toCategory(): Category {
            return Category(
                id = this.id,
                name = this.name?:"",
                description = this.description,
                listBookCategory = emptyList()
            )
        }
    }
}
