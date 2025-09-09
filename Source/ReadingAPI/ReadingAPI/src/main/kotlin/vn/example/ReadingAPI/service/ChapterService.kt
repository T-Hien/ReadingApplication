package vn.example.ReadingAPI.service

import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import vn.example.ReadingAPI.model.Chapter
import vn.example.ReadingAPI.repository.ChapterRepository

@Service
@Transactional
class ChapterService(private var chapterRps: ChapterRepository) {
    fun getChapterByNum(bookId:Int, chapterId:Int): Chapter {
        return chapterRps.findByChapternumberAndAbook_Id(chapterId,bookId)
    }
    fun getChapterByBookId(bookId:Int):List<Chapter>{
        return chapterRps.findByAbook_Id(bookId)
    }
    fun deleteAllByBookId(bookId:Int){
        chapterRps.deleteByAbook_Id(bookId)
    }
}