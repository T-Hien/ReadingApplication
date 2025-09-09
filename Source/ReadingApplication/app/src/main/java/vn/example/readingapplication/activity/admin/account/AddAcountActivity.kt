package vn.example.readingapplication.activity.admin.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.admin.UserManagementActivity
import vn.example.readingapplication.adapter.account.ImageAdapter
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityAddAccountBinding
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class AddAcountActivity : BaseActivity()
{
    private lateinit var binding: ActivityAddAccountBinding
    private var role:Int = 0
    private val listUser = mutableListOf<User>()


    private var check:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)
        getInforUser()
        val role = intent.getIntExtra("role",-1)
        binding.btnImg.setOnClickListener(){
            showImageSelectionDialog()

        }
        binding.btnAdd.setOnClickListener(){
            if(!binding.edtPassword.text.toString().equals(binding.edtPasswordAgain.text.toString())){
                Toast.makeText(this@AddAcountActivity,"Mật khẩu và nhập lại mật khẩu không trùng!",Toast.LENGTH_SHORT).show()
            }
            else{
                val txtUsername = binding.edtUsername.text.toString()
                val txtEmail = binding.edtEmail.text.toString()
                var checkUsername = false
                var checkmail = false
                for(item in listUser){
                    if(item.username ==txtUsername){
                        checkUsername = true
                    }
                    if (item.email == txtEmail){
                        checkmail = true
                    }
                }
                if(!checkUsername &&!checkmail){
                    createAccount(role)

                }
                else if(checkUsername){
                    Toast.makeText(this@AddAcountActivity,"Tên tài khoản đã tồn tại!",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@AddAcountActivity,"Email đã tồn tại!",Toast.LENGTH_SHORT).show()
                }
            }

        }
        binding.btnBack.setOnClickListener(){
            finish()
        }
    }
    private fun checkout(){
        val intent = Intent(this, UserManagementActivity::class.java)
        startActivity(intent)
    }
    private fun getInforUser() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getAllUser2().enqueue(object : Callback<ResultResponse<List<User>>> {
            override fun onResponse(call: Call<ResultResponse<List<User>>>, response: Response<ResultResponse<List<User>>>) {
                if (response.isSuccessful) {
                    val users = response.body()?.dataList
                    if (users != null) {
                        for (userList in users) {
                            for (user in userList) {
                                listUser.add(user)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<List<User>>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
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
            status = 1,
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
                        this@AddAcountActivity,
                        "Thêm thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                    Thread.sleep(1000)
                    checkout()
                } else {
                    Toast.makeText(
                        this@AddAcountActivity,
                        "Thêm thất bạI!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Toast.makeText(this@AddAcountActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
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
            Glide.with(this@AddAcountActivity)
                .load(imgUrl)
                .placeholder(R.drawable.bg_user_sun_2)
                .error(R.drawable.bg_user_sun)
                .into(binding.imgUser)
            getInforUser()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
}
