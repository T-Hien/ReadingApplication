package vn.example.ReadingAPI.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "book")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(nullable = false, length = 100)
    val title: String,

    @Column(name = "created_at", updatable = false)
    val createdAt:LocalDateTime = LocalDateTime.now(),

    val description: String,
    val cover_image: String?,
    @Column(length = 100)
    val type_file: String,
    @Column(length = 20)
    val status: String,

    val active: Int,

    @OneToMany(mappedBy = "abook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val listReadingProgress: List<ReadingProgress?>,

    @OneToOne(mappedBy = "abook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val favorite: Favorites? = null,

    @OneToMany(mappedBy = "abook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val listNotification: List<Notification> = mutableListOf(),

    @OneToMany(mappedBy = "abook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val listDetailAuthor: List<DetailAuthor> = mutableListOf(),

    @OneToMany(mappedBy = "abook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val listBookCategory: List<BookCategory> = mutableListOf(),

    @OneToMany(mappedBy = "abook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val listChapter: List<Chapter> = mutableListOf(),

    @OneToMany(mappedBy = "abook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val listNote: List<Note> = mutableListOf(),

    @OneToMany(mappedBy = "abook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val listBookmark: List<Bookmark> = mutableListOf(),
    @OneToMany(mappedBy = "abook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val listLike: List<Likes> = mutableListOf()
){
    override fun toString(): String {
        return "Book(id=$id, title='$title', coverImage='$cover_image', createdAt=$createdAt, description='$description', status='$status', typeFile='$type_file')"
    }
}
