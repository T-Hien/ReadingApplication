package vn.example.readingapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.FirebaseApp
import vn.android.myekyc.utils.extension.adaptViewForInserts
import vn.example.readingapplication.adapter.ViewPagerAdapter
import vn.example.readingapplication.databinding.ActivityMainBinding
import vn.example.readingapplication.fragment.AccountFragment
import vn.example.readingapplication.fragment.HomePageFragment
import vn.example.readingapplication.fragment.LibraryFragment
import vn.example.readingapplication.fragment.NotificationFragment
import vn.example.readingapplication.fragment.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var username: String = ""
    object SharedPreferenceHelper {
        private const val PREF_NAME = "SaveAccount"

        fun getUsername(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("username", "0") ?: "0"
        }
    }
    private val fragmentMap = mapOf(
        R.id.action_home to 0,
        R.id.action_find to 1,
        R.id.action_book to 2,
        R.id.action_bell to 3,
        R.id.action_user to 4
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.background_night_v2))
        setContentView(binding.root)
        username = SharedPreferenceHelper.getUsername(this)
        //F
        FirebaseApp.initializeApp(this)
//        //FC
//        System.setProperty(
//            "GOOGLE_APPLICATION_CREDENTIALS",
//            applicationContext.resources.getResourceName(R.raw.service_account)
//        )

        handleDeepLink(intent)

        supportActionBar?.hide()
        binding.rlRoot.adaptViewForInserts()
        val apiResult = intent.getStringExtra("api_result")
        apiResult?.let {
            processApiResult(it) // Xử lý dữ liệu API nhận được
        }
        setupViewPager()
    }
    private fun processApiResult(data: String) {
        // Xử lý dữ liệu từ API (ví dụ: cập nhật giao diện hoặc lưu trữ vào database)
        Toast.makeText(this, "Received data: $data", Toast.LENGTH_SHORT).show()
    }

    private fun setupViewPager() {
        val listFragment = listOf(
            HomePageFragment(),
            SearchFragment(),
            LibraryFragment(),
            NotificationFragment(),
            AccountFragment()
        )
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, listFragment)
        binding.vpListToolbar.adapter = adapter
        binding.vpListToolbar.offscreenPageLimit = listFragment.size

        // Đồng bộ ViewPager với BottomNavigationView
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            fragmentMap[item.itemId]?.let {
                binding.vpListToolbar.currentItem = it
                true
            } ?: false
        }

        binding.vpListToolbar.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNav.selectedItemId = fragmentMap.entries.find { it.value == position }?.key ?: R.id.action_home
            }
        })

        binding.vpListToolbar.isUserInputEnabled = false
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // Cập nhật intent hiện tại
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        if (Intent.ACTION_VIEW == intent.action) {
            intent.data?.let { deepLinkUri ->
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? AccountFragment
                fragment?.handleDeepLink(deepLinkUri)
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}
