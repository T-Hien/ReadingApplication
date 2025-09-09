package vn.example.ReadingAPI.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "note")
data class Note(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,

    @Column(length = 255)
    val content: String?,

    val chapternumber: Int?,

    @ManyToOne
    @JoinColumn(name = "book_id")
    val abook: Book?,

    @ManyToOne
    @JoinColumn(name = "username")
    val auser: User?,

    @NotNull
    @Column(length = 10)
    val type: String? = null,

    val status:Int,
    @JoinColumn(name = "description")
    val description:String,

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToOne(mappedBy = "note", cascade = [CascadeType.ALL])
    val bookmark: Bookmark?,

    @OneToMany(mappedBy = "note")
    val listReplies: List<Replies>
)
