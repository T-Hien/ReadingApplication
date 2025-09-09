package vn.example.readingapplication.activity.admin.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.admin.UserManagementActivity
import vn.example.readingapplication.adapter.account.ImageAdapter
import vn.example.readingapplication.databinding.ActivityAddAccountBinding
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class UpdateAcountActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityAddAccountBinding
    private var role:Int = 0
    private var imgUrl:String =""
    private var checkDialog:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountBinding.inflate(layoutInflater)

        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.txtStyle.text = "Cập nhật thông tin"
        binding.txtStyle2.text = "Cập nhật thông tin"
        binding.btnAdd.text = "Cập nhật"
        val username = intent.getStringExtra("username",)
        val role = intent.getIntExtra("role",-1)
        binding.edtUsername.isEnabled = false
        if (username != null) {
            getInforUser(username)
        }
        binding.btnAdd.setOnClickListener(){
            createAccount(role)
        }
        binding.btnImg.setOnClickListener(){
            showImageSelectionDialog()
        }
        binding.btnBack.setOnClickListener(){
            finish()
        }
    }
    private fun checkout(){
        val intent = Intent(this,UserManagementActivity::class.java)
        startActivity(intent)
    }
    private fun getInforUser(username:String) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getUser(username).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(call: Call<ResultResponse<User>>, response: Response<ResultResponse<User>>) {
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    user?.let {
                        imgUrl = user.image.toString()
                        binding.edtName.setText(user.name.toString())
                        binding.edtUsername.setText(user.username)
                        binding.edtEmail.setText(user.email.toString())
                        binding.edtPhone.setText(user.phone.toString())
                        binding.edtPasswordAgain.setText(user.password.toString())
                        binding.edtPassword.setText(user.password.toString())
                        Glide.with(this@UpdateAcountActivity)
                            .load(imgUrl) // Load ảnh từ URL
                            .placeholder(R.drawable.bg_user_sun_2)
                            .error(R.drawable.bg_user_sun)
                            .into(binding.imgUser)
                    }

                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }
    private fun showImageSelectionDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout_img_user, null)
        dialogBuilder.setView(dialogView)

        val gridView = dialogView.findViewById<GridView>(R.id.gridView)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val okButton = dialogView.findViewById<Button>(R.id.okButton)

        val images = listOf(
            "https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleGirl.jpg",
            "https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/img_girl2.jpg",
            "https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/img_boy2.jpg",
            "https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg"
        )

        gridView.adapter = ImageAdapter(this, images)
        val alertDialog = dialogBuilder.create()

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Xử lý khi người dùng chọn một ảnh
            val selectedImage = images[position]
            imgUrl = when (position) {
                0 -> "https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleGirl.jpg"
                1 -> "https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/img_girl2.jpg"
                2 -> "https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/img_boy2.jpg"
                else -> "https://raw.githubusercontent.com/T-Hien/ReadingApplication/main/Image/User/imgLitleBoy.jpg"
            }
            Log.d("SelectedImage", "Selected image URL: $selectedImage")
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        okButton.setOnClickListener {
            checkDialog = true
            Glide.with(this@UpdateAcountActivity)
                .load(imgUrl) // Load ảnh từ URL
                .placeholder(R.drawable.bg_user_sun_2)
                .error(R.drawable.bg_user_sun)
                .into(binding.imgUser)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun createAccount(role:Int) {
        val user = User(
            username = binding.edtUsername.text.toString(),
            name = binding.edtName.text.toString(),
            role = role,
            listLike = emptyList(),
            listReadingProgress = emptyList(),
            email = binding.edtEmail.text.toString(),
            image = imgUrl,
            phone = binding.edtPhone.text.toString().toInt(),
            status = 0,
            listNote = emptyList(),
            password = binding.edtPassword.text.toString(),
            setting = null,
            listBookmark = emptyList(),
            listSearch = emptyList(),
            listReplies = emptyList()

        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createUser(user).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(
                call: Call<ResultResponse<User>>,
                response: Response<ResultResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    Toast.makeText(
                        this@UpdateAcountActivity,
                        "Thêm thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                    Thread.sleep(1000)
                    checkout()
                } else {
                    Toast.makeText(
                        this@UpdateAcountActivity,
                        "Thêm thất bạI!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Toast.makeText(this@UpdateAcountActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}
