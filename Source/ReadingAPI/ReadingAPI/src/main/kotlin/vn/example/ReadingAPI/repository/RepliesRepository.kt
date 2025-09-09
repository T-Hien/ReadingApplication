package vn.example.ReadingAPI.repository

import org.springframework.data.jpa.repository.JpaRepository
import vn.example.ReadingAPI.model.Replies
import vn.example.ReadingAPI.model.Search

interface RepliesRepository : JpaRepository<Replies, Long>{
    fun findAllByNoteId(noteId: Int): List<Replies>
    fun deleteAllByNote_Id(noteId:Int)

}
