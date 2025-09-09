package vn.example.ReadingAPI.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import org.jetbrains.annotations.NotNull


@Entity
@Table(name = "searches")
data class Search(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "username")
    val auser: User,

    @NotNull
    @Column(length = 10)
    val type: String? = null,

    @NotNull
    @Column(length = 100)
    val keyword: String? = null
)
