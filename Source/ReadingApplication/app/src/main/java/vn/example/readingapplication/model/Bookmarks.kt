package vn.example.readingapplication.model

data class Bookmarks(
    val id: Int?,
    val note: Note?,
    val auser: User?,
    val abook: Book?,
    val progress_percentage: String?,
    val position: Int,
    val type: String?,
    val chapternumber: Int?,
    val createdAt: String?
)
