package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.BookCategory
import vn.example.ReadingAPI.model.BookCategoryId
import vn.example.ReadingAPI.model.Category
import vn.example.ReadingAPI.model.Replies

interface BookCategoryRepository : JpaRepository<BookCategory, BookCategoryId> {
    fun findByAbook_Id(bookId: Int): List<BookCategory>

    fun findByCategory_Id(id: Int): List<BookCategory>

    fun findByCategory_IdAndAbook_Active(categoryId: Int, active: Int): List<BookCategory>


    fun findAllByAbook_Id(id: Int): List<BookCategory>
    @Modifying
    @Transactional
    @Query("DELETE FROM BookCategory f WHERE f.abook.id = :bookId")
    fun deleteByAbook_Id(bookId: Int)

}

