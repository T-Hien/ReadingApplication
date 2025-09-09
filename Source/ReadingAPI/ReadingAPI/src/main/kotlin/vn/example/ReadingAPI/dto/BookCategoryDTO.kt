package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.model.BookCategory
import vn.example.ReadingAPI.model.BookCategoryId
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.CategoryRepository

data class BookCategoryDTO(
    val abook: BookDTO?,
    val category: CategoryDTO?
) {
    companion object {
        fun from(bookCategories: List<BookCategory>): List<BookCategoryDTO> {
            return bookCategories.map { bookCategory ->
                BookCategoryDTO(
                    abook = bookCategory.abook?.let { BookDTO.getBook(it) },
                    category = bookCategory.category?.let { CategoryDTO.from(it) }
                )
            }
        }
        fun fromCate(bookCategories: BookCategory): BookCategoryDTO? {
            return BookCategoryDTO(
                    abook = bookCategories.abook?.let { BookDTO.getBook(it) },
                    category = bookCategories.category?.let { CategoryDTO.from(it) }
                )
            }
        fun getCategory(bookCategories: List<BookCategory>): List<BookCategoryDTO> {
            return bookCategories.map { bookCategory ->
                BookCategoryDTO(
                    abook = null,
                    category = bookCategory.category?.let { CategoryDTO.from(it) }
                )
            }
        }
        fun BookCategoryDTO.toBook(bookRepository: BookRepository, categoryRepository: CategoryRepository): BookCategory {
            val book = this.abook?.let { bookRepository.findById(it.id).orElse(null) }
            val category = this.category?.let { categoryRepository.findById(it.id).orElse(null) }

            if (book == null || category == null) {
                throw IllegalArgumentException("Book or Category not found")
            }
            return BookCategory(
                id = BookCategoryId(bookId = book.id, categoryId = category.id),
                abook = book,
                category = category
            )
        }
    }
}
