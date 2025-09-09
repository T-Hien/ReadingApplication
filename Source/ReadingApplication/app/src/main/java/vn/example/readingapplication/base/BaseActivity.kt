package vn.example.readingapplication.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("selected_language", "en")
        super.attachBaseContext(updateLocale(newBase, languageCode ?: "en"))
    }

    private fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}
