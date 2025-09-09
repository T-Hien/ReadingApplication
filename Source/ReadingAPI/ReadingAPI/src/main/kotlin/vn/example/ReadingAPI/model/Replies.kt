package vn.example.ReadingAPI.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "Replies")
data class Replies(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    val note: Note,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    val auser: User?,

    @NotNull
    @Column(nullable = false)
    val content: String,

    @NotNull
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
