package vn.example.ReadingAPI.model


import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "notifications")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "book_id")
    val abook: Book,

    @field:NotNull
    val message: String? = null,

    @field:NotNull
    @Column(length = 10)
    val type: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null

)
