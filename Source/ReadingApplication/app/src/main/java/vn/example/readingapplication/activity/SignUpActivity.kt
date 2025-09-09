package vn.example.readingapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivitySignUpBinding
import vn.example.readingapplication.model.Setting
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var checkAccount = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignUp.setOnClickListener {
            check()
            if(checkAccount){
                createAccount()
            }
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("activity", 2)
            startActivity(intent)
            finish()
        }

    }
    private fun createAccount(){
        val img = "https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg"
        val phoneText = binding.edtPhone.text.toString()
        val password = binding.edtPasswordAgain.text.toString()
        val userDTO = User(
            username = binding.edtUsername.text.toString(),
            name = binding.edtName.text.toString(),
            password = password,
            phone = phoneText.toInt(),
            email = binding.edtEmail.text.toString(),
            role = 1,
            status = 1,
            image = img,
            setting = null,
            listSearch = null,
            listReplies = null,
            listNote = null,
            listBookmark = null,
            listReadingProgress = null
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createUser(userDTO).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(
                call: Call<ResultResponse<User>>,
                response: Response<ResultResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null) {
                        if (resultResponse.status == 200) {
                            createSetting()
                            Toast.makeText(this@SignUpActivity, getString(R.string.text_create_account), Toast.LENGTH_LONG).show()
                            val intent = Intent(this@SignUpActivity,LoginActivity::class.java)
                            startActivity(intent)
                        } else if (resultResponse.status == 409) {
                            Toast.makeText(this@SignUpActivity, resultResponse.message, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@SignUpActivity, getString(R.string.title_name_account_user)+": ${userDTO.username}  đã tồn tại", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@SignUpActivity, "Null response body", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "Tạo tài khoản: ${userDTO.username} không thành công!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ","${t.message}")
            }
        })
    }
    private fun createSetting() {
        val setting = Setting(
            id = null,
            user = User(username = binding.edtUsername.text.toString()),
            font_size = 15,
            font = "Roboto",
            readingMode = "Ban ngày"
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createSetting(setting).enqueue(object : Callback<ResultResponse<Setting>> {
            override fun onResponse(
                call: Call<ResultResponse<Setting>>,
                response: Response<ResultResponse<Setting>>
            ) {
                if (response.isSuccessful) {
                }
            }
            override fun onFailure(call: Call<ResultResponse<Setting>>, t: Throwable) {
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun check() {
        if (binding.edtName.text.isEmpty()) {
            Toast.makeText(this, getString(R.string.text_please_enter_name), Toast.LENGTH_LONG).show()
        }else if (binding.edtUsername.text.isEmpty()) {
            Toast.makeText(this, getString(R.string.text_please_enter_username), Toast.LENGTH_LONG).show()
        } else if (binding.edtPassword.text.isEmpty()) {
            Toast.makeText(this, getString(R.string.text_please_enter_password), Toast.LENGTH_LONG).show()
        } else if (binding.edtPasswordAgain.text.isEmpty()) {
            Toast.makeText(this, getString(R.string.text_please_enter_password_again), Toast.LENGTH_LONG).show()
        } else if (binding.edtPhone.text.isEmpty()) {
            Toast.makeText(this, getString(R.string.text_please_enter_phone), Toast.LENGTH_LONG).show()
        }else if (binding.edtPassword.text.equals(binding.edtPasswordAgain.text.toString())) {
            Toast.makeText(this, getString(R.string.text_password_and_password_again_different), Toast.LENGTH_LONG).show()
        } else if (binding.edtEmail.text.isEmpty()) {
            Toast.makeText(this, getString(R.string.text_please_enter_email), Toast.LENGTH_LONG).show()
        } else {
            checkAccount = true
        }
    }

}