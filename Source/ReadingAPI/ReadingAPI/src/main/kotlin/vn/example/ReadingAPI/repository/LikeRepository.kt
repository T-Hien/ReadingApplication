package vn.example.ReadingAPI.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.LikeId
import vn.example.ReadingAPI.model.Likes

interface LikeRepository : JpaRepository<Likes, LikeId>{
    @Modifying
    @Transactional
    @Query("DELETE FROM Likes f WHERE f.abook.id = :bookId")
    fun deleteAllByAbook_Id(bookId:Int)

    fun deleteByAbook_IdAndAuser_Username(bookId:Int,username:String)
    fun findByAbook_IdAndAuser_Username(bookId:Int,username:String):Likes
    fun findByAbook_IdOrderByCreatedAtDesc(bookId:Int):List<Likes>
    fun findByAuser_UsernameOrderByCreatedAtDesc(username:String):List<Likes>

}


