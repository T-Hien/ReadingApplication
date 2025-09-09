package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Notification
import vn.example.ReadingAPI.repository.NotificationRepository

@Service
class NotificationService(private val notificationRsp:NotificationRepository) {
    fun getAllNotification(active:Int): List<Notification> {
        return notificationRsp.findAllByAbook_Active(active)
    }
    @Transactional
    fun deleteAllByBook(bookId:Int){
        notificationRsp.deleteAllByAbook_Id(bookId)
    }


}