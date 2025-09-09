package vn.example.ReadingAPI.model

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import org.jetbrains.annotations.NotNull


@Entity
@Table(name = "category")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @NotNull
    @Column(nullable = false, length = 100)
    val name: String,

    @Column(length = 255)
    val description: String? = null,

    @OneToMany(mappedBy = "category")
    val listBookCategory:List<BookCategory>
)
