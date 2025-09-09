package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.dto.SettingDTO.Companion.toSetting2
import vn.example.ReadingAPI.model.*
import vn.example.ReadingAPI.repository.UserRepository
import java.io.Serializable

data class UserDTO(
    val username: String?,
    val name: String? = null,
    val password: String?,
    val phone: String?,
    val email: String?,
    val role: Int?,
    val status: Int?,
    val image: String? = null,
    val setting: SettingDTO?,
    val listSearch: List<Search?>?,
    val listReplies: List<Replies?>?,
    val listNote: List<Note?>?,
    val listBookmark: List<Bookmark?>?,
    val listReadingProgress: List<ReadingProgress?>?,
    val listLike: List<Likes?>?
)
{
    companion object {
        fun from(user: User): UserDTO {
            return UserDTO(
                username = user.username,
                name = user.name,
                password = user.password,
                phone = user.phone.toString(),
                email = user.email,
                role = user.role,
                status = user.status,
                image = user.image,
                setting = user.setting?.let { SettingDTO.from(it) },
                listSearch = user.listSearch,
                listReplies = user.listReplies,
                listNote = user.listNote,
                listBookmark = user.listBookmark,
                listReadingProgress = user.listReadingProgress,
                listLike = user.listLike
            )
        }
        fun fromUser(user: User): UserDTO? {
            return UserDTO(
                username = user.username,
                name = user.name,
                password = user.password,
                phone = user.phone?.toString(),
                email = user.email,
                role = user.role,
                status = user.status,
                image = user.image,
                setting = null,
                listSearch = null,
                listReplies = null,
                listNote = null,
                listBookmark = null,
                listReadingProgress = emptyList(),
                listLike = emptyList()
            )
        }
        fun fromUser2(user: User): UserDTO {
            return UserDTO(
                username = user.username,
                name = user.name,
                password = user.password,
                phone = user.phone?.toString(),
                email = user.email,
                role = user.role,
                status = user.status,
                image = user.image,
                setting = null,
                listSearch = null,
                listReplies = null,
                listNote = null,
                listBookmark = null,
                listReadingProgress = emptyList(),
                listLike = emptyList()
            )
        }
        fun getUserRole(user: List<User>): List<UserDTO?> {
            return if (user != null) {
                user.map {user ->
                    UserDTO(
                username = user.username,
                name = user.name,
                password = user.password,
                phone = user.phone?.toString(),
                email = user.email,
                role = user.role,
                status = user.status,
                image = user.image,
                setting = null,
                listSearch = null,
                listReplies = null,
                listNote = null,
                listBookmark = null,
                listReadingProgress = emptyList(),
                listLike = emptyList()
            )}} else {
                emptyList()
            }
        }
        fun getUserEmpty(user:User):UserDTO{
            return UserDTO(
                username = user.username,
                name = user.name,
                password = user.password,
                phone = user.phone.toString(),
                email = user.email,
                role = user.role,
                status = user.status,
                image = user.image,
                setting =  user.setting?.let { SettingDTO.from(it) },
                listSearch = emptyList(),
                listReplies = emptyList(),
                listNote = emptyList(),
                listBookmark = emptyList(),
                listReadingProgress = emptyList(),
                listLike = emptyList()
            )
        }
        //Support note theo chương
        fun getUserChap(user:User):UserDTO{
            return UserDTO(
                username = user.username,
                name = user.name,
                password = null,
                phone = null,
                email = null,
                role = null,
                status = null,
                image = user.image,
                setting =  null,
                listSearch = emptyList(),
                listReplies = emptyList(),
                listNote = emptyList(),
                listBookmark = emptyList(),
                listReadingProgress = emptyList(),
                listLike = emptyList()
            )
        }
        fun UserDTO.toUser(userRepository: UserRepository): User {
            return User(
                username = this.username.toString(),
                name = this.name.toString(),
                password = this.password.toString(),
                phone = this.phone?.toInt(),
                email = this.email,
                role = this.role,
                status = this.status,
                image = this.image,
                setting = null,
                listSearch = emptyList(),
                listReplies = emptyList(),
                listNote = emptyList(),
                listBookmark = emptyList(),
                listReadingProgress = emptyList(),
                listLike = emptyList()
            )
        }
    }
}
