package vn.example.readingapplication.activity.admin.category

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.admin.CategoryManagementActivity
import vn.example.readingapplication.databinding.ActivityAdminCategoryAddBinding
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class AddCategoryActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityAdminCategoryAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCategoryAddBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.lnDeath.visibility = View.GONE

        binding.btnAddBook.setOnClickListener(){
            createCategory()
        }
        binding.btnBack.setOnClickListener(){

            finish()
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun checkout(){
        val intent = Intent(this, CategoryManagementActivity::class.java)
            startActivity(intent)
    }

    private fun createCategory() {
        val category = Category(
            id = null,
            name = binding.edtName.text.toString(),
            description = binding.edtDescription.text.toString(),
            listBookCategory = null
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createCategory(category).enqueue(object : Callback<ResultResponse<Category>> {
            override fun onResponse(
                call: Call<ResultResponse<Category>>,
                response: Response<ResultResponse<Category>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    Toast.makeText(
                        this@AddCategoryActivity,
                        "Thêm thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                    Thread.sleep(1000)
                    checkout()
                } else {
                    Toast.makeText(
                        this@AddCategoryActivity,
                        "Đăng tải sách không thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResultResponse<Category>>, t: Throwable) {
                Toast.makeText(this@AddCategoryActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}
