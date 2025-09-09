package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Notification

@Repository
interface NotificationRepository : JpaRepository<Notification, Int>{
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification f WHERE f.abook.id = :bookId")
    fun deleteAllByAbook_Id(bookId:Int)

    @Query("SELECT f FROM Notification f WHERE f.abook.active = :active")
    fun findAllByAbook_Active(active:Int):List<Notification>
}
