package vn.example.readingapplication.base

import android.content.Context
import androidx.fragment.app.Fragment
import java.util.Locale
import android.R

open class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(updateLocale(context))
    }

    private fun updateLocale(context: Context): Context {
        val sharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("selected_language", "en")
        val locale = Locale(languageCode ?: "en")
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}
