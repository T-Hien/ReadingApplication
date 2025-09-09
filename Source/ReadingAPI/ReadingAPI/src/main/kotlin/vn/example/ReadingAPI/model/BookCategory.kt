package vn.example.ReadingAPI.model

import jakarta.persistence.*

@Entity
@Table(name = "book_category")
data class BookCategory(
    @EmbeddedId
    val id: BookCategoryId?,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    val abook: Book?,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category?
)
