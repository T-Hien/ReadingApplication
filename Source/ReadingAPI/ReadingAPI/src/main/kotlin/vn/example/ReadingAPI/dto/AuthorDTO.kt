package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.model.Author
import vn.example.ReadingAPI.model.Category
import vn.example.ReadingAPI.model.DetailAuthor
import java.time.LocalDate

data class AuthorDTO(
    val id: Int,
    val name: String?,
    val birth_date: LocalDate?,
    val death_date: LocalDate?,
    val listDetailAuthor: List<DetailAuthorDTO?>?
){
    companion object {
        fun from(author: Author): AuthorDTO {
            return AuthorDTO(
                id = author.id,
                name = author.name,
                birth_date = author.birth_date,
                death_date = author.death_date,
                listDetailAuthor = emptyList()
            )
        }
        fun AuthorDTO.toAuthor(): Author {
            return Author(
                id = this.id,
                name = this.name,
                birth_date = this.birth_date,
                death_date = this.death_date,
                listDetailAuthor = emptyList()
            )
        }
    }
}
