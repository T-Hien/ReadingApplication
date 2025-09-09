package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import vn.example.ReadingAPI.model.Note
import vn.example.ReadingAPI.model.Replies
import vn.example.ReadingAPI.repository.NoteRepository
import vn.example.ReadingAPI.repository.RepliesRepository

@Service
class RepliesService(private var replyRsp:RepliesRepository) {
    fun deleteByNoteId(noteId: Int) {
        val replies = replyRsp.findAllByNoteId(noteId)
        replyRsp.deleteAll(replies)
    }
    fun deleteAllByNote(noteId:Int){
        replyRsp.deleteAllByNote_Id(noteId)
    }
}