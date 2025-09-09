package vn.example.ReadingAPI.model



import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import org.jetbrains.annotations.NotNull
import java.time.LocalDate

@Entity
@Table(name = "author")
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @NotNull
    @Column(nullable = false, length = 100)
    val name: String?=null,

    val birth_date: LocalDate? = null,
    val death_date: LocalDate? = null,

    @OneToMany(mappedBy = "author")
    val listDetailAuthor:List<DetailAuthor>?
)
