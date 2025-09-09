package vn.example.ReadingAPI.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class ReadingProgressId(

    @Column(name = "book_id")
    val bookId: Int,

    @Column(name = "username")
    val username: String,

    @Column(name = "chapter_id")
    val chapterId: Int
) : Serializable
