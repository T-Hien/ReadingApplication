package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import vn.example.ReadingAPI.dto.AuthorFavoriteDTO
import vn.example.ReadingAPI.model.Author
import vn.example.ReadingAPI.repository.AuthorFavoriteRepository
import vn.example.ReadingAPI.repository.AuthorRepository
import vn.example.ReadingAPI.repository.DetailAuthorRepository

@Service
class AuthorService(private val detailAuthorRepository: DetailAuthorRepository,
                    private val authorRsp: AuthorRepository,
    private val authorFavoriteRepository: AuthorFavoriteRepository) {

    fun getAuthorsByBookId(bookId: Int): List<Author> {
        return detailAuthorRepository.findByAbook_Id(bookId).map { it.author }
    }

    fun searchAuthorsByName(keyword: String): List<Author> {
        return authorRsp.findByNameContaining(keyword)
    }

    fun deleteByBookId(bookId: Int) {
        detailAuthorRepository.deleteByAbook_Id(bookId)
    }

    fun getTopAuthorsByFavorites(): List<AuthorFavoriteDTO> {
        return authorFavoriteRepository.findTopAuthorsByFavorites()
    }
}