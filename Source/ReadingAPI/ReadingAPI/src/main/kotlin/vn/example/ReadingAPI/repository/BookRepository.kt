package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import vn.example.ReadingAPI.model.Book

interface BookRepository : JpaRepository<Book, Int>{
    fun findTop5ByActiveOrderByIdDesc(active:Int): List<Book>
    @Query("""
    SELECT b 
    FROM Book b 
    WHERE b.active = 0 
    ORDER BY b.createdAt DESC
""")
    fun findTop20ByActiveOrderByCreatedAtDesc(): List<Book>

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword%")
    fun findByTitleContaining(@Param("keyword") keyword: String): List<Book>

    fun findByActive(active: Int): List<Book>

    @Query("SELECT COUNT(u) FROM Book u")
    fun getCount(): Int


}
