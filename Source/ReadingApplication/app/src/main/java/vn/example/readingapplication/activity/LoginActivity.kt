package vn.example.readingapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.MainActivity
import vn.example.readingapplication.R
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityLoginBinding
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient


class LoginActivity :BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    private var username:String = "0"
    private var password:String = "0"
    private var role:Int? = null
    private var checkRole:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE)
        val role2 = sharedPreferences.getInt("role", -1)
        sharedPreferences.edit().clear().apply()
        binding.btnLogin.setOnClickListener {
            if(role2 == 0 || role2 ==1){
                checkRole = true
            }
            login()
        }
        binding.btnSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnForgotPassword.setOnClickListener(){
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.btnMain.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    private fun login() {
        username = binding.edtUsername.text.toString()
        password = binding.edtPassword.text.toString()
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.text_please_write_infor_login), Toast.LENGTH_LONG).show()
            return
        } else {
            val apiService = RetrofitClient.getClient().create(ApiService::class.java)
            apiService.signIn(username, password).enqueue(object : Callback<ResultResponse<User>> {
                override fun onResponse(call: Call<ResultResponse<User>>, response: Response<ResultResponse<User>>) {
                    val status = response.body()?.status
                    if (response.isSuccessful && status == 200) {
                        Toast.makeText(this@LoginActivity, getString(R.string.text_login_successfully), Toast.LENGTH_SHORT).show()
                        role = response.body()?.dataNum
                        if(role==2){
                            saveActivity2()
                        }
                        else{
                            saveActivity()
                        }

                    }
                    else if (response.code() == 403 || status == 403) {
                        Toast.makeText(this@LoginActivity, getString(R.string.text_account_block), Toast.LENGTH_LONG).show()
                    }
                    else if(response.code() == 401 || status == 401){
                        Toast.makeText(this@LoginActivity, getString(R.string.text_account_password_failed), Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(this@LoginActivity, getString(R.string.text_account_username_failed), Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
    fun saveActivity() {

        val sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        role?.let { editor.putInt("role", it) }
        editor.apply()
        val intent = Intent(this, MainAdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun saveActivity2() {
        val sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        role?.let { editor.putInt("role", it) }
        editor.putBoolean("from_activity", true) // Ghi nhận trạng thái
        editor.apply()

        if (checkRole) {
            checkRole = false
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val fromActivity = intent.getIntExtra("activity", -1)
            if (fromActivity != 2) {
                editor.putBoolean("from_activity", false).apply()
                setResult(RESULT_OK)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }

}