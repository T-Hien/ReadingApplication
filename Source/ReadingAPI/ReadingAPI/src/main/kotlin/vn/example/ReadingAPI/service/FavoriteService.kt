package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Favorites
import vn.example.ReadingAPI.repository.FavoriteRepository

@Service
class FavoriteService(private var favoriteRps: FavoriteRepository) {
    fun getFavoriteByBookId(bookId:Int):Favorites{
        return favoriteRps.findByAbook_Id(bookId)
    }
    fun getTop20Favorites(): List<Favorites> {
        return favoriteRps.findTop20ByBookActiveOrderByNumberDesc()
    }

    @Transactional
    fun deleteAllByBook(bookId: Int) {
        println("Attempting to delete all favorites with book ID: $bookId")
        favoriteRps.deleteAllByAbook_Id(bookId)
        println("Deleted all favorites with book ID: $bookId")
    }

}