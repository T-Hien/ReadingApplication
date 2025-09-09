package vn.example.readingapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.MailjetClient
import vn.example.readingapplication.R
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityForgotPasswordBinding
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val mailjetClient = MailjetClient("364dedeb9d478b1035aee2529e02f679", "d1e67bdd12eb02c460ff2e53e0140c06")
    private var password = ""
    private var mail = ""
    private var title = ""
    private var txtPass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnForgot.setOnClickListener {
            getInforUser(binding.edtUsername.text.toString())
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("activity", 2)
            startActivity(intent)
            finish()
        }


        binding.btnSignUpFg.setOnClickListener(){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun getInforUser(username: String) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getUser(username).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(
                call: Call<ResultResponse<User>>,
                response: Response<ResultResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    user?.let {
                        val inputEmail = binding.edtEmail.text.toString()
                        mail = user.email.toString()
                        password = user.password.toString()
                        if(user.status == 1){
//                            Toast.makeText(this@ForgotPasswordActivity,"Tài khoản của bạn đã bị khóa!",Toast.LENGTH_SHORT).show()
                            binding.txtPassword.text = "Tài khoản của bạn đã bị khóa!"
                            title = "Vui lòng liên hệ với quản lý ứng dụng để mở khóa tài khoản!"
                            txtPass = "Thông tin liên hệ: n20dccn100@student.ptithcm.edu.vn"
                        }
                        else{
                            binding.txtPassword.text = ""
                            title = "Khôi phục mật khẩu ứng dụng đọc sách Phiêu"
                            txtPass = "Mật khẩu của bạn: ${password}"
                        }
                        if (user.email.toString().equals(inputEmail)) {
//                            binding.txtPassword.text = user.password.toString()
                            sendEmail()

                        } else {
                            binding.txtPassword.visibility = View.VISIBLE
                            binding.txtPassword.text = getString(R.string.text_please_write_phone)
                        }
                    }
                } else {
                    binding.txtPassword.visibility = View.VISIBLE
                    binding.txtPassword.text = getString(R.string.text_please_write_account)
                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

    private fun sendEmail() {
        mailjetClient.sendEmail(
            to = "${mail}",
            subject = "$title",
            text = "$txtPass"
        ) { success, errorMessage ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, getString(R.string.text_email_sent_successfully), Toast.LENGTH_SHORT).show()
                } else {
                    // Hiển thị chi tiết lỗi từ phản hồi
                    Toast.makeText(this, "Failed to send email: $errorMessage", Toast.LENGTH_LONG).show()
                    Log.d("ERROR_EMAIL:", "$errorMessage")
                }
            }
        }
    }
}
