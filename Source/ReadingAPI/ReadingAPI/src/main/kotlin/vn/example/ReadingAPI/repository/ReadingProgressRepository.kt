package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.ReadingProgress
import vn.example.ReadingAPI.model.ReadingProgressId


interface ReadingProgressRepository : JpaRepository<ReadingProgress, ReadingProgressId>{
    fun findByAuser_Username(username: String): List<ReadingProgress>
    fun findByAuser_UsernameAndStatus(username: String, status: String): List<ReadingProgress>
    //Bo
    fun findByAuser_UsernameAndAbook_Id(username: String,bookId:Int):ReadingProgress?

    fun findByAuser_UsernameAndAbook_IdAndAchapter_Id(username: String,bookId:Int,chapId:Int):ReadingProgress?
    @Query("SELECT rp FROM ReadingProgress rp WHERE rp.auser.username = :username AND rp.abook.id = :bookId AND rp.achapter.chapternumber = :chapNum")
    fun findByAuser_UsernameAndAbook_IdAndAchapter_Chapternumber(
    @Param("username") username: String,
    @Param("bookId") bookId: Int,
    @Param("chapNum") chapNum: Int
    ): ReadingProgress?

    @Modifying
    @Transactional
    @Query("DELETE FROM ReadingProgress f WHERE f.abook.id = :bookId")
    fun deleteAllByAbook_Id(bookId:Int)

    @Query("SELECT COUNT(u) FROM ReadingProgress u")
    fun getCount(): Int
}
