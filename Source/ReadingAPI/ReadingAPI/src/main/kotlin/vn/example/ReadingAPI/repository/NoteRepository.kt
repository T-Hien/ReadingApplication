package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Note

interface NoteRepository : JpaRepository<Note, Int>{
    fun findByTypeAndAbook_Id(type:String,bookId:Int):List<Note>

    fun findByTypeAndAbook_IdAndChapternumberOrderByCreatedAtDesc(type: String, bookId: Int, chapnum: Int): List<Note>
    fun findTopByOrderByIdDesc(): Note?

    fun findByTypeOrderByCreatedAtDesc(type: String): List<Note>

    fun findByTypeAndStatusOrderByCreatedAtDesc(type: String,status:Int): List<Note>


    fun findByTypeAndAbook_IdAndAuser_Username(type:String,bookId:Int,username:String):List<Note>
    fun findByTypeAndAbook_IdAndAuser_UsernameAndChapternumber(type:String,bookId:Int,username:String,chapternum:Int):List<Note>


    @Modifying
    @Transactional
    @Query("DELETE FROM Note f WHERE f.abook.id = :bookId")
    fun deleteAllByAbook_Id(bookId:Int)
}
