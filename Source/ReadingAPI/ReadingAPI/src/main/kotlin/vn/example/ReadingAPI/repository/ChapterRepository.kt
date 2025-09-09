package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Chapter

interface ChapterRepository : JpaRepository<Chapter, Int>{
    fun findByIdAndAbook_Id(chapterId:Int,bookId:Int):Chapter
    fun findByAbook_Id(bookId:Int):List<Chapter>
    fun findByChapternumberAndAbook_Id(chapter_number: Int, bookId: Int): Chapter

    @Modifying
    @Transactional
    @Query("DELETE FROM Chapter f WHERE f.abook.id = :bookId")
    fun deleteByAbook_Id(bookId:Int)
}
