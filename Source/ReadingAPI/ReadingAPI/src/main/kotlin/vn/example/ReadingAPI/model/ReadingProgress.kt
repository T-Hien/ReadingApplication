package vn.example.ReadingAPI.model

import java.time.LocalDateTime
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "reading_progress")
data class ReadingProgress(

    @EmbeddedId
    val id: ReadingProgressId?,

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username", nullable = false)
    val auser: User,

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id", nullable = false)
    val abook: Book,

    @ManyToOne
    @MapsId("chapterId")
    @JoinColumn(name = "chapter_id", nullable = false)
    val achapter: Chapter,

    @NotNull
    val status: String,

    @NotNull
    val progressPercentage: Float,

    @NotNull
    val progressPath: String,

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now()
)
