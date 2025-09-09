package vn.example.readingapplication.model

import java.io.Serializable

data class Book(
    val id: Int?,
    val title: String? = null,
    val createdAt: String? = null,
    val description: String? = null,
    val cover_image: String? = null,
    val type_file: String? = null,
    val status: String? = null,
    val active : Int?=0,
    val listReadingProgress: List<ReadingProgress>? = null,
    val favorite: Favorite? = null,
    val listNotification: List<Notification>? = null,
    val listDetailAuthor: List<DetailAuthor>? = null,
    val listBookCategory: List<BookCategory>? = null,
    val listChapter: List<Chapter>? = null,
    val listNote: List<Note>? = null,
    val listBookmark: List<Bookmarks>? = null,
    val listLike: List<Like?>? = null,
): Serializable