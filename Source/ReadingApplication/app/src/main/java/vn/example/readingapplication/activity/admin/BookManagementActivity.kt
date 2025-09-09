package vn.example.readingapplication.activity.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import vn.android.myekyc.utils.extension.adaptViewForInserts
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.MainAdminActivity
import vn.example.readingapplication.adapter.ViewPagerAdapter
import vn.example.readingapplication.adapter.admin.book.BookFragment
import vn.example.readingapplication.databinding.ActivityAdminBookManagementBinding

class BookManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBookManagementBinding
    companion object {
        const val REQUEST_CODE_UPDATE_BOOK = 1001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBookManagementBinding.inflate(layoutInflater)

        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainAdminActivity::class.java)
            startActivity(intent)
        }
        val currentTab = intent.getIntExtra("CURRENT_TAB", 0)
        binding.rlRoot.adaptViewForInserts()
        val listFragment = listOf(
            BookFragment.newInstance(0),
            BookFragment.newInstance(1)
        )

        binding.vpList.offscreenPageLimit = listFragment.size

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, listFragment)
        binding.vpList.adapter = adapter
        val tabTitles = arrayOf("Sách", "Sách bị khóa")

        TabLayoutMediator(binding.tlList, binding.vpList) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        binding.vpList.setCurrentItem(currentTab, false)
    }

    override fun onRestart() {
        super.onRestart()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UPDATE_BOOK && resultCode == RESULT_OK) {
            // Reload dữ liệu từ ViewPager Adapter
            val fragments = supportFragmentManager.fragments
            for (fragment in fragments) {
                if (fragment is BookFragment) {
                    fragment.reloadData() // Gọi hàm reloadData() trong BookFragment
                }
            }
        }
    }


    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}