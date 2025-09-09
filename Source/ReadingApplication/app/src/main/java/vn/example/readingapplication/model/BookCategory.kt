package vn.example.readingapplication.model

data class BookCategory(
    val id :BookCategoryId? = null,
    val abook: Book?,
    val category: Category?
)
