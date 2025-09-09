package vn.example.ReadingAPI.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import vn.example.ReadingAPI.dto.AuthorFavoriteDTO
import vn.example.ReadingAPI.dto.ReadingProgressCountDTO
import vn.example.ReadingAPI.dto.ReadingProgressDTO
import java.sql.ResultSet

@Repository
class ReadingProgressCountRepository (val jdbcTemplate: JdbcTemplate) {

    fun findTopReadingProgressCount(): List<ReadingProgressCountDTO> {
        val sql = "CALL GetReadingProgressCountByUser()"
        return jdbcTemplate.query(sql, RowMapper { rs: ResultSet, _: Int ->
            ReadingProgressCountDTO(
                username = rs.getString("username"),
                name = rs.getString("name"),
                reading_count = rs.getInt("reading_count")
            )
        })
    }
}