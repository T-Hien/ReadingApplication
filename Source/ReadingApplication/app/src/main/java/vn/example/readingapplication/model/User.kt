package vn.example.readingapplication.model

import java.io.Serializable

data class User(
    val username: String,
    val name: String? = null,
    val password: String?=null,
    val email: String? = null,
    val phone: Int? = null,
    val role: Int? = null,
    val status: Int? = null,
    val image: String? = null,
    val setting: Setting? = null,
    val listSearch: List<Search>? = null,
    val listLike: List<Like?>? = null,
    val listReplies: List<Replies>? = null,
    val listNote: List<Note>? = null,
    val listBookmark: List<Bookmarks>? = null,
    val listReadingProgress: List<ReadingProgress>? = null
): Serializable

