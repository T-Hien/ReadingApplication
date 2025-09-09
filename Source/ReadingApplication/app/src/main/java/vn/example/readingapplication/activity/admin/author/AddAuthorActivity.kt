package vn.example.readingapplication.activity.admin.author

import android.app.DatePickerDialog
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
import vn.example.readingapplication.databinding.ActivityAdminCategoryAddBinding
import vn.example.readingapplication.model.Author
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddAuthorActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityAdminCategoryAddBinding
    private var check:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminCategoryAddBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.txtText1.text = "Tên tác giả"
        binding.edtName.hint = "Nhập tên tác giả"
        binding.lnDes.visibility = View.GONE
        binding.txtStyleBook.text = "Thêm tác giả"
        binding.txtStyleCategoryName.text = "Thêm tác giả"
        binding.lnDeath.visibility = View.VISIBLE
        binding.btnAddBook.setOnClickListener(){
            createCategory()
        }
        binding.btnBack.setOnClickListener(){
            val intent = Intent(this,AuthorManagementActivity::class.java)
            startActivity(intent)
        }
        binding.btnBirth.setOnClickListener {
            check = true
            showDatePickerDialog()
        }
        binding.btnDeath.setOnClickListener {
            showDatePickerDialog()
        }
    }
    private fun checkout(){
        val intent = Intent(this,AuthorManagementActivity::class.java)
        startActivity(intent)
    }
    private fun createCategory() {
        val author = Author(
            id = null,
            name = binding.edtName.text.toString(),
            birth_date = binding.edtBirth.text.toString(),
            death_date = binding.edtDeath.text.toString(),
            listDetailAuthor = null
        )
        Log.d("INF","$author")
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createAuthor(author).enqueue(object : Callback<ResultResponse<Author>> {
            override fun onResponse(
                call: Call<ResultResponse<Author>>,
                response: Response<ResultResponse<Author>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    Toast.makeText(
                        this@AddAuthorActivity,
                        "Thêm thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                    Thread.sleep(1000)
                    checkout()
                } else {
                    Toast.makeText(
                        this@AddAuthorActivity,
                        "Thêm tác giả thất bạI!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResultResponse<Author>>, t: Throwable) {
                Toast.makeText(this@AddAuthorActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                if(check){
                    binding.edtBirth.setText(dateFormat.format(selectedDate.time))
                    check = false
                }
                else{
                    binding.edtDeath.setText(dateFormat.format(selectedDate.time))
                }
            },
            year, month, day
        )
        datePickerDialog.show()
    }
    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}
