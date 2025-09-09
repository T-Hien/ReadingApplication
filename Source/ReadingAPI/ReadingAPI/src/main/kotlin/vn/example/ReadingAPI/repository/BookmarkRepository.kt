package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Bookmark

interface BookmarkRepository : JpaRepository<Bookmark, Int>{
    fun findByAUser_Username(username: String):List<Bookmark>

    @Modifying
    @Transactional
    @Query("DELETE FROM Bookmark f WHERE f.abook.id = :bookId")
    fun deleteAllByAbook_Id(bookId:Int)

    fun deleteByNote_Id(noteId:Int)

    fun findByChapternumberAndTypeAndAbook_IdAndAUser_UsernameOrderByIdDesc(chapternumber:Int,type:String,bookId:Int,username:String):List<Bookmark>
}
