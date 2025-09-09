package vn.example.ReadingAPI.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.RowMapper
import vn.example.ReadingAPI.dto.AuthorFavoriteDTO
import vn.example.ReadingAPI.model.Author
import java.sql.ResultSet

interface AuthorRepository : JpaRepository<Author, Int>
{
//    fun findByBookId(book_id: String): Book?
    @Query(value = "SELECT * FROM Author WHERE name LIKE %:keyword%", nativeQuery = true)
    fun findByNameContaining(@Param("keyword") keyword: String): List<Author>

    @Query("SELECT COUNT(u) FROM Author u")
    fun getCount(): Int

}


