package vn.example.readingapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.adapter.account.ImageAdapter
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityEditAccountBinding
import vn.example.readingapplication.fragment.AccountFragment
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class EditAccountActivity : BaseActivity() {
    private lateinit var binding: ActivityEditAccountBinding
    private var username = ""
    private var checkUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = this.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        val username2 = sharedPreferences?.getString("username", null)
        val role = sharedPreferences?.getInt("role", -1)
        if (username2 != null) {
            username = username2
        }
        getInforUser()

        binding.btnEditPicture.setOnClickListener {
            showImageSelectionDialog()
        }
        binding.btnUpdate.setOnClickListener {
            updateUser()
        }
        binding.btnExit.setOnClickListener {
            if (checkUpdate && role ==2) {
                val intent = Intent(this,AccountFragment::class.java)
                startActivity(intent)
            }
            finish()
        }
    }
    private fun updateUser() {
        var phoneText: String = binding.edtPhone.text.toString()

        val userDTO = User(
            username = username,
            name = binding.edtName.text.toString(),
            password = binding.edtPassword.text.toString(),
            phone = phoneText.toInt(),
            email = binding.edtEmail.text.toString(),
            role = binding.txtRole.text.toString().toInt(),
            status = 1,
            image = imgUrl,
            setting = null,
            listSearch = null,
            listReplies = null,
            listNote = null,
            listLike = null,
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
                            checkUpdate = true
                            Toast.makeText(this@EditAccountActivity, "Cập nhật thông tin thành công!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@EditAccountActivity, resultResponse.message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@EditAccountActivity, "Null response body", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@EditAccountActivity, "Cập nhật thông tin tài khoản: ${userDTO.username} không thành công!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Toast.makeText(this@EditAccountActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERROREditAccount: ", "${t.message}")
            }
        })
    }
    private var imgUrl:String =""
    private var checkDialog:Boolean = false


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
            getInforUser()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun getInforUser() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getUser(username).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(
                call: Call<ResultResponse<User>>,
                response: Response<ResultResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    user?.let {
                        binding.edtUsername.setText(username)
                        binding.edtUsername.isEnabled = false
                        if(!checkDialog){
                            imgUrl = user.image.toString()
                        }
                        Glide.with(this@EditAccountActivity)
                            .load(imgUrl)
                            .placeholder(R.drawable.bg_library_admin)
                            .error(R.drawable.img_book_conan)
                            .into(binding.imgUser)
                        binding.edtName.setText(user.name?:"Unknow")
                        binding.edtEmail.setText(user.email)
                        binding.edtPhone.setText(user.phone.toString())
                        binding.edtPassword.setText(user.password)
                        binding.edtPasswordAgain.setText(user.password)
                        binding.txtRole.text = user.role.toString()
                    }

                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }
    private fun check() {
        if (binding.edtUsername.text.isEmpty()) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_LONG).show()
            return
        }
        if (binding.edtPassword.text.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show()
            return
        }
        if (binding.edtPasswordAgain.text.isEmpty()) {
            Toast.makeText(this, "Please enter password again", Toast.LENGTH_LONG).show()
            return
        }
        if (binding.edtPhone.text.isEmpty()) {
            Toast.makeText(this, "Please enter phone", Toast.LENGTH_LONG).show()
            return
        }
        if (binding.edtEmail.text.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show()
            return
        }
    }
}