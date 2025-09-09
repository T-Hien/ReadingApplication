package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Favorites

interface FavoriteRepository : JpaRepository<Favorites, Int> {
    fun findByAbook_Id(bookId: Int): Favorites

    @Query("""
    SELECT f FROM Favorites f 
    WHERE f.abook.active = 0 
    ORDER BY f.number DESC
""")
    fun findTop20ByBookActiveOrderByNumberDesc(): List<Favorites>


    @Modifying
    @Transactional
    @Query("DELETE FROM Favorites f WHERE f.abook.id = :bookId")
    fun deleteAllByAbook_Id(bookId: Int)
}
