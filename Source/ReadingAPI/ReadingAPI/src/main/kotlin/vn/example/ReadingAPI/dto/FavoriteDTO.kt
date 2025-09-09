package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.model.Favorites
import vn.example.ReadingAPI.repository.BookRepository

data class FavoriteDTO (
    val id: Int?,
    val abook: BookDTO?,
    val number: Int?
){
    companion object {
        fun from(favorites: List<Favorites>): List<FavoriteDTO> {
            return favorites.map { favorite ->
                FavoriteDTO(
                    id = favorite.id,
                    abook = null,
                    number = favorite.number
                )
            }
        }

        fun fromFavorite(favorites: Favorites): FavoriteDTO {
            return FavoriteDTO(
                id = favorites.id,
                abook = null,
                number = favorites.number
            )
        }

        fun getFavoriteTop(favorite: Favorites): FavoriteDTO {
            return FavoriteDTO(
                id = favorite.id,
                abook = BookDTO.getCategory(favorite.abook),
                number = favorite.number
            )
        }
        fun FavoriteDTO.toFavorite(bookRepository: BookRepository): Favorites? {
            val book = this.abook?.let { bookRepository.findById(it.id).orElse(null) }
            return if (book != null) {
                Favorites(
                    id = this.id,
                    abook = book,
                    number = this.number
                )
            } else {
                null
            }
        }

    }
}