package vn.example.ReadingAPI.dto

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import vn.example.ReadingAPI.model.Note
import vn.example.ReadingAPI.model.Search
import vn.example.ReadingAPI.model.User
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.UserRepository
import java.io.Serializable
import java.time.LocalDateTime

data class SearchDTO(
    val id: Int?,
    val auser: UserDTO?,
    val type: String?,
    val keyword: String?
):Serializable{
    companion object{
        fun getSearch(search:Search):SearchDTO{
            return SearchDTO(
                id = search.id,
                auser = null,
                type = search.type,
                keyword = search.keyword
            )
        }


        fun SearchDTO.toSearch(userRepository: UserRepository): Search? {
            val user = this.auser?.username?.let { userRepository.findByUsername(it) }
            return if (user != null) {
                Search(
                    id = this.id,
                    auser = user,
                    type = this.type,
                    keyword = this.keyword

                )
            } else {
                null
            }
        }
    }
}
