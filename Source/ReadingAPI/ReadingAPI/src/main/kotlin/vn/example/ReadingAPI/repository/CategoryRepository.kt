package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import vn.example.ReadingAPI.model.Category
import vn.example.ReadingAPI.model.Replies

interface CategoryRepository : JpaRepository<Category, Int>{
    @Query("SELECT b FROM Category b WHERE b.name LIKE %:keyword%")
    fun findByNameContaining(@Param("keyword") keyword: String): List<Category>

    fun findByName(name:String):Category?

    @Query("SELECT COUNT(u) FROM Category u")
    fun getCount(): Int

}

