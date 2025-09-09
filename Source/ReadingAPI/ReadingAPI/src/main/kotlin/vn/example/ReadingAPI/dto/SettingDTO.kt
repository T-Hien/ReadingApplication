package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.model.Setting
import vn.example.ReadingAPI.repository.UserRepository
import java.io.Serializable

data class SettingDTO(
    val id: Int? = null,
    val user: UserDTO?,
    val font: String? = null,
    val font_size: Int? = null,
    val readingMode: String? = null
):Serializable{
    companion object {
        fun from(setting: Setting): SettingDTO {
            return SettingDTO(
                id = setting.id,
                user = null,
                font = setting.font,
                font_size = setting.font_size,
                readingMode = setting.readingMode
            )
        }
        fun SettingDTO.toSetting2(userRepository: UserRepository): Setting {
            val user = this.user?.username?.let { userRepository.findByUsername(it) }
            return Setting(
                id = this.id,
                user = user,
                font = this.font,
                font_size = this.font_size,
                readingMode = this.readingMode
            )
        }
    }
}
