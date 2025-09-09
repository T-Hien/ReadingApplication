package vn.example.ReadingAPI.model


import jakarta.persistence.*
import lombok.NoArgsConstructor
import org.jetbrains.annotations.NotNull
import vn.example.ReadingAPI.dto.NoteDTO
import java.time.LocalDateTime

@Entity
@NoArgsConstructor
@Table(name = "bookmarks")
data class Bookmark(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @OneToOne
    @JoinColumn(name = "note_id")
    val note: Note?,

    @ManyToOne
    @JoinColumn(name = "username")
    val aUser: User?,

    @ManyToOne
    @JoinColumn(name = "book_id")
    val abook: Book,

    @NotNull
    val progress_percentage: String? = null,

    @NotNull
    val position: Int? = null,

    @NotNull
    @Column(length = 10)
    val type: String,

    val chapternumber: Int?,

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
