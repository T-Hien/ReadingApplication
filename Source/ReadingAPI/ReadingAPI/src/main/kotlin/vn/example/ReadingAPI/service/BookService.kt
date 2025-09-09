package vn.example.ReadingAPI.service

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Service
import vn.example.ReadingAPI.model.Book
import vn.example.ReadingAPI.model.BookCategory
import vn.example.ReadingAPI.model.Category
import vn.example.ReadingAPI.repository.BookCategoryRepository
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.DetailAuthorRepository
import java.util.*

@Service
class BookService(private var bookRps:BookRepository,
                  private var detailAuthorRepository: DetailAuthorRepository,
                    private var bookCategoryRepository: BookCategoryRepository) {

    fun showAll(): List<Book> {
        return bookRps.findAll()
    }

    fun showAll2(active:Int): List<Book> {
        // Lọc các sách có active = 0
        return bookRps.findByActive(active)
    }
    fun findTop5ByOrderByIdDesc(active:Int):List<Book>{
        return bookRps.findTop5ByActiveOrderByIdDesc(active)
    }
    fun findTop20ByOrderByCreatedAtDesc():List<Book>{
        return bookRps.findTop20ByActiveOrderByCreatedAtDesc()
    }
    @Query("SELECT * FROM books WHERE id = :bookId", nativeQuery = true)
    fun getBookById(@Param("bookId") bookId: Int): Optional<Book>{
        return bookRps.findById(bookId)
    }
    fun searchBooksByTitle(keyword: String): List<Book> {
        return bookRps.findByTitleContaining(keyword)
    }
    fun getBookByAuthor(authorId:Int):List<Book>{
        return detailAuthorRepository.findByAuthor_Id(authorId).map { it.abook }
    }

    fun getBookByCategory(categoryId:Int):List<Book>{
        return bookCategoryRepository.findByCategory_Id(categoryId).map { it.abook!! }
    }
}