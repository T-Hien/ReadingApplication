package vn.example.ReadingAPI.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull


@Entity
@Table(name = "favorites")
data class Favorites(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @OneToOne
    @JoinColumn(name = "book_id")
    val abook: Book,

    @NotNull
    val number: Int? = null,
){
    override fun toString(): String {
        return "Favorite(id=$id, abookId=${abook.id}, number=$number)"
    }
}
