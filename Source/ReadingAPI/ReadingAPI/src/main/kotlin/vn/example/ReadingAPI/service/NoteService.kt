package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Note
import vn.example.ReadingAPI.repository.NoteRepository
import java.util.*

@Service
class NoteService(private var noteRps:NoteRepository) {
    fun getNoteByTypeAndBook(type:String,bookId:Int):List<Note>{
        return noteRps.findByTypeAndAbook_Id(type, bookId)
    }
    fun getNoteByTypeAndChap(type:String,bookId:Int,chapnum:Int):List<Note>{
        return noteRps.findByTypeAndAbook_IdAndChapternumberOrderByCreatedAtDesc(type, bookId,chapnum)
    }

    fun getLargestNoteId(): Int? {
        return noteRps.findTopByOrderByIdDesc()?.id
    }
    fun getNoteByType(type:String):List<Note>{
        return noteRps.findByTypeOrderByCreatedAtDesc(type)
    }

    fun getNoteByTypeAndStatus(type:String,status:Int):List<Note>{
        return noteRps.findByTypeAndStatusOrderByCreatedAtDesc(type,status);
    }

    fun getNoteById(noteId:Int): Optional<Note> {
        return noteRps.findById(noteId)
    }

    fun getNoteByTypeAndBookAndUser(type:String,bookId:Int,username:String):List<Note>{
        return noteRps.findByTypeAndAbook_IdAndAuser_Username(type, bookId, username)
    }
    fun getNoteByTypeAndBookAndUserAndChap(type:String,bookId:Int,username:String,chapnum:Int):List<Note>{
        return noteRps.findByTypeAndAbook_IdAndAuser_UsernameAndChapternumber(type, bookId, username,chapnum)
    }


    @Transactional
    fun deleteAllByBook(bookId:Int){
        noteRps.deleteAllByAbook_Id(bookId)
    }


}