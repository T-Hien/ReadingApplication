package vn.example.readingapplication.activity.admin.category

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
import vn.example.readingapplication.activity.admin.AuthorManagementActivity
import vn.example.readingapplication.activity.admin.CategoryManagementActivity
import vn.example.readingapplication.databinding.ActivityAdminCategoryAddBinding
import vn.example.readingapplication.model.Author
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class UpdateCategoryActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityAdminCategoryAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCategoryAddBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.btnAddBook.text = "Cập nhật"
        val p = intent.getIntExtra("P",-1)
        val id = intent.getIntExtra("ID",-1)
        val name = intent.getStringExtra("NAME")
        val description = intent.getStringExtra("DESCRIPTION")
        val birth = intent.getStringExtra("BIRTH")
        val death = intent.getStringExtra("DEATH")
        Log.d("TTT","$id, $name,$birth,$death")

        if(p==1){
            binding.lnDes.visibility = View.GONE
            binding.txtStyleBook.text = "Cập nhật tác giả"
            binding.txtStyleCategoryName.text = "Cập nhật tác giả"
            binding.edtName.setText(name.toString())
            binding.edtBirth.setText(birth.toString())
            binding.edtDeath.setText(death.toString())
        }
        if(p==2){
            binding.txtStyleBook.text = "Cập nhật thể loại"
            binding.txtStyleCategoryName.text = "Cập nhật thể loại"
            binding.lnDeath.visibility = View.GONE
            binding.edtName.setText(name.toString())
            binding.edtDescription.setText(description.toString())
        }
        binding.btnBack.setOnClickListener(){
            finish()
        }
        binding.btnAddBook.setOnClickListener(){
            if(p==2){
                val name = binding.edtName.text.toString()
                val descrip = binding.edtDescription.text.toString()
                if(name.equals("")){
                    Toast.makeText(this,"Tên thể loại không được để trống!",Toast.LENGTH_SHORT).show()
                }
                else if(name.equals("")){
                    Toast.makeText(this,"Mô tả thể loại không được để trống!",Toast.LENGTH_SHORT).show()
                }
                else{
                    updateCategory(id,name,descrip)
                }
            }
            else if(p==1){
                val name = binding.edtName.text.toString()
                if(name.equals("")){
                    Toast.makeText(this,"Tên thể loại không được để trống!",Toast.LENGTH_SHORT).show()
                }
                else{
                    updateAuthor(id)
                }
            }
        }

    }
    private fun out(){
        val intent = Intent(this, AuthorManagementActivity::class.java)
        startActivity(intent)
    }
    private fun out2(){
        val intent = Intent(this, CategoryManagementActivity::class.java)
        startActivity(intent)
    }
    private fun updateCategory(id:Int,name:String, des:String) {
        val category = Category(
            id = id,
            name = name,
            description = des,
            listBookCategory = null
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.updateCategory(category).enqueue(object : Callback<ResultResponse<Category>> {
            override fun onResponse(
                call: Call<ResultResponse<Category>>,
                response: Response<ResultResponse<Category>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
                        Toast.makeText(
                            this@UpdateCategoryActivity,
                            "Cập nhật thành công!",
                            Toast.LENGTH_LONG
                        ).show()
                        Thread.sleep(800)
                        out2()
                    } else {
                        Toast.makeText(
                            this@UpdateCategoryActivity,
                            "Chỉnh sửa thất bại!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@UpdateCategoryActivity,
                        "Chỉnh sửa thất bại!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResultResponse<Category>>, t: Throwable) {
                Toast.makeText(this@UpdateCategoryActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun updateAuthor(id:Int) {
        if (binding.edtBirth.text.toString() == "null") {
            binding.edtBirth.setText("")
        }
        if (binding.edtDeath.text.toString() == "null") {
            binding.edtDeath.setText("")
        }
        val birthDate = binding.edtBirth.text.toString().takeIf { it.isNotBlank() }
        val deathDate = binding.edtDeath.text.toString().takeIf { it.isNotBlank() }
        val author = Author(
            id = id,
            name = binding.edtName.text.toString(),
            birth_date = birthDate,
            death_date = deathDate,
            listDetailAuthor = null
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createAuthor(author).enqueue(object : Callback<ResultResponse<Author>> {
            override fun onResponse(
                call: Call<ResultResponse<Author>>,
                response: Response<ResultResponse<Author>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    Toast.makeText(
                        this@UpdateCategoryActivity,
                        "Cập nhật thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                    Thread.sleep(800)
                    out()
                } else {
                    Toast.makeText(
                        this@UpdateCategoryActivity,
                        "Cập nhật thất bạI!",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("ERRORResponse", "Error: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<ResultResponse<Author>>, t: Throwable) {
                Toast.makeText(this@UpdateCategoryActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }

    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}
