package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Favorites
import vn.example.ReadingAPI.model.Likes
import vn.example.ReadingAPI.repository.FavoriteRepository
import vn.example.ReadingAPI.repository.LikeRepository

@Service
@Transactional
class LikeService(private var likeRepository: LikeRepository) {
    fun deleteAllByBook(bookId:Int){
        likeRepository.deleteAllByAbook_Id(bookId)
    }

    fun deleteByBookAndUser(bookId:Int,username:String){
        likeRepository.deleteByAbook_IdAndAuser_Username(bookId, username)
    }
    fun findByBookAndUser(bookId:Int,username:String): Likes {
        return likeRepository.findByAbook_IdAndAuser_Username(bookId, username)
    }

    fun findByBook(bookId:Int): List<Likes> {
        return likeRepository.findByAbook_IdOrderByCreatedAtDesc(bookId)
    }
    fun findByUsername(username:String): List<Likes> {
        return likeRepository.findByAuser_UsernameOrderByCreatedAtDesc(username)
    }
}