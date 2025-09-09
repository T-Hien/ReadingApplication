package vn.example.readingapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.admin.AuthorManagementActivity
import vn.example.readingapplication.activity.admin.BookManagementActivity
import vn.example.readingapplication.activity.admin.CategoryManagementActivity
import vn.example.readingapplication.activity.admin.CommentManagementActivity
import vn.example.readingapplication.activity.admin.StatisticalActivity
import vn.example.readingapplication.activity.admin.UserManagementActivity
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityMainAdminBinding
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class MainAdminActivity : BaseActivity() {
    private lateinit var binding:ActivityMainAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        val sharedPreferences = this.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        val username2 = sharedPreferences?.getString("username", null)
        val role = sharedPreferences?.getInt("role", -1)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)
        if (username2 != null) {
            getInforUser(username2)
        }

        if(role==1){
            binding.btnAccount.visibility = View.GONE
        }
        binding.btnBook.setOnClickListener(){
            val intent = Intent(this, BookManagementActivity::class.java)
            startActivity(intent)
        }

        binding.btnAuthor.setOnClickListener(){
            val intent = Intent(this, AuthorManagementActivity::class.java)
            startActivity(intent)
        }
        binding.btnCategory.setOnClickListener(){
            val intent = Intent(this, CategoryManagementActivity::class.java)
            startActivity(intent)
        }
        binding.btnComment.setOnClickListener(){
            val intent = Intent(this, CommentManagementActivity::class.java)
            startActivity(intent)
        }
        binding.btnAccount.setOnClickListener(){
            val intent = Intent(this, UserManagementActivity::class.java)
            startActivity(intent)
        }
        binding.btnStatic.setOnClickListener(){
            val intent = Intent(this, StatisticalActivity::class.java)
            startActivity(intent)
        }
        binding.btnAdmin.setOnClickListener(){
            val intent = Intent(this, EditAccountActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun getInforUser(username:String) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getUser(username).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(
                call: Call<ResultResponse<User>>,
                response: Response<ResultResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.image
                    Glide.with(this@MainAdminActivity)
                        .load(imageUrl) // URL hình ảnh từ server
                        .placeholder(R.drawable.placeholder_image) // Hình ảnh tạm trong khi tải
                        .error(R.drawable.error_image) // Hình ảnh khi tải thất bại
                        .into(binding.btnAdmin) // Gán hình ảnh vào btnAdmin
                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }
    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}