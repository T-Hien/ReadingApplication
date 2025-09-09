package vn.example.ReadingAPI.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import vn.example.ReadingAPI.dto.AuthorFavoriteDTO
import java.sql.ResultSet

@Repository
class AuthorFavoriteRepository (val jdbcTemplate: JdbcTemplate) {

    fun findTopAuthorsByFavorites(): List<AuthorFavoriteDTO> {
        val sql = "CALL GetTopAuthorsByFavorites()"
        return jdbcTemplate.query(sql, RowMapper { rs: ResultSet, _: Int ->
            AuthorFavoriteDTO(
                id = rs.getInt("id"),
                author_name = rs.getString("author_name"),
                total_favorites = rs.getInt("total_favorites")
            )
        })
    }
}