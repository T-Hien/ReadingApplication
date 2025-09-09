package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Bookmark
import vn.example.ReadingAPI.repository.BookmarkRepository

@Service
class BookmarkService(private var bookmarkRsp:BookmarkRepository) {

    fun getBookmarkByUsername(username:String):List<Bookmark>{
        return bookmarkRsp.findByAUser_Username(username)
    }
    @Transactional
    fun deleteAllByBook(bookId:Int){
        bookmarkRsp.deleteAllByAbook_Id(bookId)
    }
    fun deleteByNote(noteId:Int){
        bookmarkRsp.deleteByNote_Id(noteId)
    }

    fun getBookmarkByBookAndUser(chapternumber:Int,type:String,bookId:Int,username:String):List<Bookmark>{
        return bookmarkRsp.findByChapternumberAndTypeAndAbook_IdAndAUser_UsernameOrderByIdDesc(chapternumber,type, bookId, username)
    }
}