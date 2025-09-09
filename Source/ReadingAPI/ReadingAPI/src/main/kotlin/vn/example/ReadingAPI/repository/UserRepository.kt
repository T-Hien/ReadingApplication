package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import vn.example.ReadingAPI.model.User
import java.util.*

interface UserRepository : JpaRepository<User, String> {
    fun findByUsernameAndPassword(username: String, password: String): User?
    override fun findAll(): MutableList<User>
    fun findByUsername(username: String): User

    fun findByRole(role:Int):List<User>

    fun findByRoleAndStatus(role:Int, status:Int):List<User>

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    fun getCountByRole(@Param("role") role: Int): Int



}
