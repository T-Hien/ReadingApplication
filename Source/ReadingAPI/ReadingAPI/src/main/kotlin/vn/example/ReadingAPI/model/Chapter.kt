package vn.example.ReadingAPI.model


import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "chapter")
data class Chapter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "book_id")
    val abook: Book?,

    @Column(length = 100)
    val title: String?,

    @NotNull
    @Column(name = "chapter_number")
    val chapternumber: Int? = null,

    @Column(name = "created_at", updatable = false)
    val createdAt:LocalDateTime = LocalDateTime.now(),

    @NotNull
    val file_path: String? = null,

    @OneToMany(mappedBy = "achapter", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val listReadingProgress: List<ReadingProgress?>

)
