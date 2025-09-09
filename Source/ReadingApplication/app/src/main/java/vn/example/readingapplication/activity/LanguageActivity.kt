package vn.example.readingapplication.activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import vn.example.readingapplication.MainActivity
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityLanguageBinding
import java.util.Locale

class LanguageActivity : BaseActivity() {
    private lateinit var binding: ActivityLanguageBinding

    private var currentLanguage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentLanguage = resources.configuration.locales[0].language

        binding.cbEnglish.isChecked = currentLanguage == "en"
        binding.cbVietnamese.isChecked = currentLanguage == "vi"

        binding.cbEnglish.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && currentLanguage != "en") {
                binding.cbVietnamese.isChecked = false
                setLocale("en")
            }
        }

        binding.cbVietnamese.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && currentLanguage != "vi") {
                binding.cbEnglish.isChecked = false
                setLocale("vi")
            }
        }
    }

    private fun restartApp() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("app_settings", MODE_PRIVATE).edit()
        editor.putString("selected_language", lang)
        editor.apply()

        currentLanguage = lang
        restartApp()
    }
}
