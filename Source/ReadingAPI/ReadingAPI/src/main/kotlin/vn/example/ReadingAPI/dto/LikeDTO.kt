package vn.example.ReadingAPI.dto

import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import vn.example.ReadingAPI.dto.SettingDTO.Companion.toSetting2
import vn.example.ReadingAPI.model.*
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.UserRepository
import java.time.LocalDateTime

data class LikeDTO(
    val abook: BookDTO?,
    val auser: UserDTO?,
    val createdAt:LocalDateTime? = null,
) {
    companion object {
        fun getLike(like: Likes):LikeDTO{
            return LikeDTO(
                abook = null,
                auser = null,
                createdAt = like.createdAt
            )

        }
        fun getListLike(likes: List<Likes>?): List<LikeDTO>? {
            return likes?.mapNotNull { like ->
                LikeDTO(
                    abook = null,
                    auser = like.auser?.let { UserDTO.getUserChap(it) },
                    createdAt = like.createdAt
                )
            }
        }

        fun getListLikeByUser(likes: List<Likes>?): List<LikeDTO>? {
            return likes?.mapNotNull { like ->
                LikeDTO(
                    abook = like.abook?.let { BookDTO.getAllBook2(it) },
                    auser = null,
                    createdAt = like.createdAt
                )
            }
        }


        fun LikeDTO.toLike(bookRepository: BookRepository,userRepository: UserRepository): Likes? {
            val book = this.abook?.let { bookRepository.findById(it.id).orElse(null) }
            val user = this.auser?.username?.let { userRepository.findByUsername(it) }
            return if (book != null &&user !=null) {
                Likes(
                    id = LikeId(bookId = book.id, username = user.username),
                    abook = book,
                    auser = user
                )
            } else {
                null
            }
        }
    }
}
