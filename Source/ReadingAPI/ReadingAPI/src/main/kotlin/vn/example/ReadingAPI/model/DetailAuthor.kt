package vn.example.ReadingAPI.model

import jakarta.persistence.*

@Entity
@Table(name = "detail_author")
data class DetailAuthor(
    @EmbeddedId
    val id: DetailAuthorId,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    val abook: Book,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("author")
    @JoinColumn(name = "author_id")
    val author: Author
)
