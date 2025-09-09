package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import vn.example.ReadingAPI.dto.CategoryFavoriteDTO
import vn.example.ReadingAPI.model.Category
import vn.example.ReadingAPI.repository.CategoryFavoriteRepository
import vn.example.ReadingAPI.repository.CategoryRepository
import java.util.Optional

@Service
class CategoryService(private var categoryRepository: CategoryRepository,
                      private val categoryFavoriteRepository: CategoryFavoriteRepository) {

    fun getAllCategory(): MutableList<Category> {
        return categoryRepository.findAll()
    }

    fun searchCategoriesByName(keyword: String): List<Category> {
        return categoryRepository.findByNameContaining(keyword)
    }
    fun getTopCategoriesByFavorites(): List<CategoryFavoriteDTO> {
        return categoryFavoriteRepository.findTopCategoriesByFavorites()
    }
}