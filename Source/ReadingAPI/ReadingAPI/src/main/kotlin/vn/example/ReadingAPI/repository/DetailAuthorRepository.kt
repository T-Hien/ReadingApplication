package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.DetailAuthor
import vn.example.ReadingAPI.model.DetailAuthorId

interface DetailAuthorRepository : JpaRepository<DetailAuthor, DetailAuthorId> {
    fun findByAbook_Id(bookId: Int): List<DetailAuthor>
    fun findByAuthor_Id(authorId:Int):List<DetailAuthor>

    @Modifying
    @Transactional
    @Query("DELETE FROM DetailAuthor f WHERE f.abook.id = :bookId")
    fun deleteByAbook_Id(bookId: Int)
}
