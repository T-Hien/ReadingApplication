package vn.example.readingapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.databinding.ActivityForgotPasswordBinding
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ForgotPasswordActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnForgot.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            getInforUser(username)
        }

        binding.btnLogin.setOnClickListener {
            val sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUpFg.setOnClickListener {
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
                        if (user.email.toString().equals(inputEmail)) {
                            binding.txtPassword.text = user.password.toString()
                        } else {
                            binding.txtPassword.visibility = View.VISIBLE
                            binding.txtPassword.text = "Số điện thoại không đúng. \nVui lòng nhập đúng số điện thoại."
                        }
                    }
                } else {
                    binding.txtPassword.visibility = View.VISIBLE
                    binding.txtPassword.text = "Tên đăng nhập không đúng. \nVui lòng nhập đúng tên đăng nhập."
                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }
}
