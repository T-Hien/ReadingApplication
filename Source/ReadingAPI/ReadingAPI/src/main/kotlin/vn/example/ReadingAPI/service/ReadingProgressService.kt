package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.dto.AuthorFavoriteDTO
import vn.example.ReadingAPI.dto.ReadingProgressCountDTO
import vn.example.ReadingAPI.model.ReadingProgress
import vn.example.ReadingAPI.repository.ReadingProgressCountRepository
import vn.example.ReadingAPI.repository.ReadingProgressRepository

@Service
@Transactional
data class ReadingProgressService(private var readingRsp:ReadingProgressRepository,
    private var readingCountRps:ReadingProgressCountRepository){
    fun getReadingProgressByUsername(username: String): List<ReadingProgress> {
        return readingRsp.findByAuser_Username(username)
    }
    fun getReadingByUsernameAndStatus(username:String, status:String):List<ReadingProgress>{
        return readingRsp.findByAuser_UsernameAndStatus(username,status)
    }
    //Bo
    fun getReadingByIdUsernameBookId(username: String,bookId:Int):ReadingProgress?{
        return readingRsp.findByAuser_UsernameAndAbook_Id(username, bookId)
    }

    fun getReadingByIdUsernameBookIdAnChapNum(username: String,bookId:Int,chapnum:Int):ReadingProgress?{
        return readingRsp.findByAuser_UsernameAndAbook_IdAndAchapter_Chapternumber(username, bookId,chapnum)
    }
    fun getReadingByIdUsernameBookIdAnChapId(username: String,bookId:Int,chapnum:Int):ReadingProgress?{
        return readingRsp.findByAuser_UsernameAndAbook_IdAndAchapter_Id(username, bookId,chapnum)
    }
    fun deleteAllByBook(bookId:Int){
        readingRsp.deleteAllByAbook_Id(bookId)
    }

    fun getTopReadingProgress(): List<ReadingProgressCountDTO> {
        return readingCountRps.findTopReadingProgressCount()
    }
}
