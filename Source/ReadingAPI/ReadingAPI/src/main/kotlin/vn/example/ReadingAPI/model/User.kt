package vn.example.ReadingAPI.model

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "user")
data class User(

    @Id
    @Column(nullable = false, unique = true, length = 100)
    val username: String,
    @Column(length = 100)
    val name: String? = null,

    @Column(nullable = false, length = 100)
    val password: String?,

    @Column(unique = true, length = 100)
    val email: String? = null,

    @Column(length = 10)
    val phone: Int?,
    val role: Int?,
    val status: Int?,

    @Column(length = 255)
    val image: String? = null,

    @Transient
    @OneToOne(mappedBy = "user")
    val setting: Setting?,

    @Transient
    @OneToMany(mappedBy = "auser")
    val listSearch: List<Search?>?,
    @OneToMany(mappedBy = "auser")
    val listLike: List<Likes?>?,

    @OneToMany(mappedBy = "auser")
    val listReplies: List<Replies?>?,

    @OneToMany(mappedBy = "auser")
    val listNote:List<Note?>?,

    @OneToMany(mappedBy = "aUser")
    val listBookmark:List<Bookmark?>?,

    @OneToMany(mappedBy = "auser")
    val listReadingProgress: List<ReadingProgress?>?


)
