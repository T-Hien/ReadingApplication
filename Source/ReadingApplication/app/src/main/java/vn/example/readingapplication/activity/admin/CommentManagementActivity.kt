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
import vn.example.readingapplication.adapter.admin.comment.CommentFragment
import vn.example.readingapplication.adapter.admin.comment.CommentManagementAdapter
import vn.example.readingapplication.databinding.ActivityAdminBookManagementBinding
import vn.example.readingapplication.model.Note

class CommentManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBookManagementBinding
    private lateinit var adapter: CommentManagementAdapter
    private val itemList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBookManagementBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.txtStyleBook.text = "Quản lý bình luận"
        adapter = CommentManagementAdapter(itemList)
        binding.btnBack.setOnClickListener(){
            val intent = Intent(this,MainAdminActivity::class.java)
            startActivity(intent)
        }
        val currentTab = intent.getIntExtra("CURRENT_TAB", 0)
        binding.rlRoot.adaptViewForInserts()
        val listFragment = listOf(
            CommentFragment.newInstance(0),
            CommentFragment.newInstance(1)
        )

        binding.vpList.offscreenPageLimit = listFragment.size

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, listFragment)
        binding.vpList.adapter = adapter
        val tabTitles = arrayOf("Bình luận", "Bình luận đã khóa")

        TabLayoutMediator(binding.tlList, binding.vpList) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        binding.vpList.setCurrentItem(currentTab, false)
    }


    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}