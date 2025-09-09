package vn.example.readingapplication.model

import java.io.Serializable

data class Chapter(
    val id: Int?,
    val abook: Book?=null,
    val title: String?=null,
    val chapternumber: Int?=null,
    val createdAt: String?=null,
    val file_path: String?=null,
    val listReadingProgress: List<ReadingProgress>?=null
): Serializable

