package vn.example.readingapplication.activity.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.MainAdminActivity
import vn.example.readingapplication.activity.admin.category.AddCategoryActivity
import vn.example.readingapplication.adapter.admin.category.CategoryManagementAdapter
import vn.example.readingapplication.databinding.ActivityCategoryManagementBinding
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class CategoryManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryManagementBinding
    private lateinit var adapter: CategoryManagementAdapter
    private val categoryList = mutableListOf<Category>()

    // Sử dụng ActivityResultLauncher để theo dõi kết quả từ Activity khác
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryManagementBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)

        adapter = CategoryManagementAdapter(categoryList)
        binding.rcvList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding.rcvList.adapter = adapter

        getListCategories()

        // Đăng ký ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Khi quay lại từ Activity khác, cập nhật danh sách
                getListCategories()
            }
        }

        // Xử lý sự kiện nút quay lại
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainAdminActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện nút thêm danh mục
        binding.btnAddCategory.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun getListCategories() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getListCategory().enqueue(object : Callback<ResultResponse<Category>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Category>>,
                response: Response<ResultResponse<Category>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { categories ->
                        categoryList.clear()
                        categoryList.addAll(categories)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.d("ERROR", "Failed to load categories")
                }
            }

            override fun onFailure(call: Call<ResultResponse<Category>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}
