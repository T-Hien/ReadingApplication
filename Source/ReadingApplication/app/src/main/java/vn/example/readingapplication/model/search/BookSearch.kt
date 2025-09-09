package vn.example.readingapplication.model.search

import vn.example.readingapplication.model.BookCategory
import vn.example.readingapplication.model.Bookmarks
import vn.example.readingapplication.model.Chapter
import vn.example.readingapplication.model.DetailAuthor
import vn.example.readingapplication.model.Favorite
import vn.example.readingapplication.model.Like
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.model.Notification
import vn.example.readingapplication.model.ReadingProgress

data class BookSearch(
    val id: Int?,
    val title: String? = null,
    val createdAt: String? = null, // Note: String type is used for date
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
    var labels: List<String> = emptyList()

)

