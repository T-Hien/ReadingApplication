package vn.example.ReadingAPI.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull


@Entity
@Table(name = "setting")
data class Setting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @OneToOne
    @JoinColumn(name="username")
    val user: User?,

    @Column(length = 100)
    val font: String? = null,

    val font_size: Int? = null,
    @NotNull
    @Column(length = 10)
    val readingMode: String? = null
)
