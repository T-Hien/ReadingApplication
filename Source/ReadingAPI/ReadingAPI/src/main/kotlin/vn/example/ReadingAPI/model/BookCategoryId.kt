package vn.example.ReadingAPI.model

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class BookCategoryId(
    @Column(name = "book_id")
    val bookId: Int,

    @Column(name = "category_id")
    val categoryId: Int
) : Serializable
