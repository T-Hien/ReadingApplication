package vn.example.readingapplication.activity.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import vn.android.myekyc.utils.extension.adaptViewForInserts
import vn.example.readingapplication.activity.MainAdminActivity
import vn.example.readingapplication.adapter.ViewPagerAdapter
import vn.example.readingapplication.adapter.account.ReaderFragment
import vn.example.readingapplication.adapter.admin.UserManagementAdapter
import vn.example.readingapplication.databinding.ActivityUserManagementBinding
import vn.example.readingapplication.model.User

class UserManagementActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserManagementBinding
    private lateinit var adapter: UserManagementAdapter
    private val listUser = mutableListOf<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener(){
            val intent = Intent(this,MainAdminActivity::class.java)
            startActivity(intent)
        }
        binding.rlRoot.adaptViewForInserts()
        val currentTab = intent.getIntExtra("CURRENT_TAB", 0)
        val listFragment = listOf(
            ReaderFragment.newInstance(2),
            ReaderFragment.newInstance(1)
        )

        binding.vpList.offscreenPageLimit = listFragment.size

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, listFragment)
        binding.vpList.adapter = adapter
        val tabTitles = arrayOf("Độc giả", "Quản lý bậc 2")

        TabLayoutMediator(binding.tlList, binding.vpList) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        binding.vpList.setCurrentItem(currentTab, false)
    }
}