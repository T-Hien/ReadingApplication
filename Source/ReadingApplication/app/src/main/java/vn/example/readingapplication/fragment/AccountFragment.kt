package vn.example.readingapplication.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.EditAccountActivity
import vn.example.readingapplication.activity.LanguageActivity
import vn.example.readingapplication.activity.LoginActivity
import vn.example.readingapplication.activity.admin.account.IntroduceActivity
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentAccountBinding
import vn.example.readingapplication.fragment.readbook.ListLikeByUserFragment
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class AccountFragment : BaseFragment() {

    private lateinit var binding: FragmentAccountBinding
    private var username = ""
    private var checkAccount = false

    companion object {
        private const val LOGIN_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()
        if (username.equals("0")) {
            binding.lnName.visibility = View.GONE
            binding.btnLogout.text = getString(R.string.button_login)
        } else {
            getInforUser()
            checkAccount = true
        }
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        updateAccountInfo()
    }

    private fun setupListeners() {
        binding.btnLogout.setOnClickListener {
            if (!checkAccount) {
                startActivityForResult(
                    Intent(context, LoginActivity::class.java),
                    LOGIN_REQUEST_CODE
                )
            } else {
                val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.remove("username")
                editor?.remove("role")
                editor?.apply()
                username = sharedPreferences?.getString("username", "0").toString()
                checkAccount = false
                if (username.equals("0")) {
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                }
            }
        }
        binding.btnEditAccount.setOnClickListener {
            startActivity(Intent(context, EditAccountActivity::class.java))
        }
        binding.lnEditShare.setOnClickListener {
            shareDeepLink()
        }
        binding.lnEditLanguage.setOnClickListener {
            startActivity(Intent(context, LanguageActivity::class.java))
        }
        binding.lnEditIntroduce.setOnClickListener() {
            startActivity(Intent(context, IntroduceActivity::class.java))
        }
        binding.lnListLike.setOnClickListener(){
            val fragment = ListLikeByUserFragment.newInstance(8)
            replaceFragment(fragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Đăng nhập thành công, cập nhật giao diện
            updateAccountInfo()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fmViewLike, fragment)
        fragmentTransaction.addToBackStack(null)
        binding.fmViewLike.visibility = View.VISIBLE

        fragmentTransaction.commit()
        childFragmentManager.addOnBackStackChangedListener {
            if (childFragmentManager.backStackEntryCount == 0) {
                binding.fmViewLike.visibility = View.GONE
            }
        }
    }
    private fun updateAccountInfo() {
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0") ?: ""
        if (!username.equals("0")) {
            getInforUser()
            checkAccount = true
            binding.lnName.visibility = View.VISIBLE
            binding.btnLogout.text = getString(R.string.button_logout)
            setupListeners()

        }
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
                        val imgUser = user.image.toString()
                        context?.let { context ->
                            Glide.with(context)
                                .load(imgUser)
                                .placeholder(R.drawable.bg_library_admin)
                                .error(R.drawable.img_book_conan)
                                .into(binding.imgUser)
                        }
                        binding.txtName.text = user.name ?: user.username
                    }
                } else {
                    Log.d("ERROR", "${response.errorBody()}")

                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

    private fun shareDeepLink() {
        val deepLinkUrl = "https://www.vivu.com/somepath"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, deepLinkUrl)
        }
        startActivity(Intent.createChooser(intent, "Chia sẻ liên kết"))
    }

    fun handleDeepLink(uri: Uri) {
        val path = uri.path
        val queryParam = uri.getQueryParameter("zarsrc")
        Toast.makeText(
            requireContext(),
            "Deep link path: $path, query: $queryParam",
            Toast.LENGTH_SHORT
        ).show()
        // Xử lý logic deep link khác tại đây
    }
}

