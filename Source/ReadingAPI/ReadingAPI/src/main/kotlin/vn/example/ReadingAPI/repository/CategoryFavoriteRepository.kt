package vn.example.ReadingAPI.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import vn.example.ReadingAPI.dto.CategoryFavoriteDTO
import java.sql.ResultSet

@Repository
class CategoryFavoriteRepository(val jdbcTemplate: JdbcTemplate) {

    fun findTopCategoriesByFavorites(): List<CategoryFavoriteDTO> {
        val sql = "CALL GetTopCategoriesByFavorites()"
        return jdbcTemplate.query(sql, RowMapper { rs: ResultSet, _: Int ->
            CategoryFavoriteDTO(
                categoryId = rs.getInt("category_id"),
                categoryName = rs.getString("category_name"),
                totalFavorites = rs.getInt("total_favorites")
            )
        })
    }
}
