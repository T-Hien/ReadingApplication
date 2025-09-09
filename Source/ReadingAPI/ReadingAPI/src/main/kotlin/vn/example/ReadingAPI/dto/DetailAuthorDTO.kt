package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.model.BookCategory
import vn.example.ReadingAPI.model.BookCategoryId
import vn.example.ReadingAPI.model.DetailAuthor
import vn.example.ReadingAPI.model.DetailAuthorId
import vn.example.ReadingAPI.repository.AuthorRepository
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.CategoryRepository
import java.io.Serializable

data class DetailAuthorDTO (
    val abook: Int,
    val author: AuthorDTO?
):Serializable
 {
     companion object {
         fun from(detailAuthor: List<DetailAuthor>): List<DetailAuthorDTO> {
             return detailAuthor.map { detailAuthor ->
                 DetailAuthorDTO(
                     abook = detailAuthor.abook.id,
                     author = AuthorDTO.from(detailAuthor.author)
                 )
             }
         }
         fun DetailAuthorDTO.toBook(bookRepository: BookRepository, authorRepository: AuthorRepository): DetailAuthor {
             val book = this.abook.let { bookRepository.findById(it).orElse(null) }
             val author = this.author?.let { authorRepository.findById(it.id).orElse(null) }

             if (book == null || author == null) {
                 throw IllegalArgumentException("Book or Author not found")
             }
             return DetailAuthor(
                 id = DetailAuthorId(bookId = book.id, author = author.id),
                 abook = book,
                 author = author
             )
         }
     }
}