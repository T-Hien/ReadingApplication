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
import vn.example.readingapplication.activity.admin.author.AddAuthorActivity
import vn.example.readingapplication.adapter.admin.author.AuthorManagementAdapter
import vn.example.readingapplication.databinding.ActivityAuthorManagementBinding
import vn.example.readingapplication.model.Author
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class AuthorManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorManagementBinding
    private lateinit var adapter: AuthorManagementAdapter
    private val itemList = mutableListOf<Author>()

    // Sử dụng ActivityResultLauncher để xử lý kết quả trả về từ AddAuthorActivity
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorManagementBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)

        // Đăng ký ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Khi quay lại từ AddAuthorActivity, cập nhật danh sách
                getListAuthors()
            }
        }

        // Thiết lập adapter và RecyclerView
        adapter = AuthorManagementAdapter(itemList)
        binding.rcvList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding.rcvList.adapter = adapter

        // Lấy danh sách tác giả ban đầu
        getListAuthors()

        // Xử lý sự kiện nút quay lại
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainAdminActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện nút thêm tác giả
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddAuthorActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun getListAuthors() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getAllAuthor().enqueue(object : Callback<ResultResponse<Author>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Author>>,
                response: Response<ResultResponse<Author>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { authors ->
                        itemList.clear()
                        itemList.addAll(authors)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.d("ERROR", "Failed to load authors")
                }
            }

            override fun onFailure(call: Call<ResultResponse<Author>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}
