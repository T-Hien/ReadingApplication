package vn.example.ReadingAPI.model

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class DetailAuthorId(
    @Column(name = "book_id")
    val bookId: Int,

    @Column(name = "author_id")
    val author:Int
) : Serializable
