package vn.example.ReadingAPI.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "likes")
data class Likes(
    @EmbeddedId
    val id: LikeId?,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    val abook: Book?,

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username", nullable = false)
    val auser: User,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
