package vn.example.readingapplication.adapter.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.activity.admin.account.AddAcountActivity
import vn.example.readingapplication.adapter.admin.UserManagementAdapter
import vn.example.readingapplication.databinding.FragmentHomeFavoritesBinding
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ReaderFragment : Fragment() {

    private lateinit var binding: FragmentHomeFavoritesBinding
    private lateinit var adapter: UserManagementAdapter
    private val listUser = mutableListOf<User>()
    private var role:Int = 0

    companion object {
        fun newInstance(role: Int): ReaderFragment {
            val fragment = ReaderFragment()
            val args = Bundle()
            args.putInt("KEY_DATA", role)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            role = it.getInt("KEY_DATA", -1)
        }

        binding = FragmentHomeFavoritesBinding.inflate(inflater, container, false)

        adapter = UserManagementAdapter(listUser)
        binding.rcvFavoritesBook.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvFavoritesBook.adapter = adapter
        getInfUser()
        binding.lnAdd.visibility = View.VISIBLE
        binding.btnAdd.setOnClickListener(){
            val intent = Intent(context,AddAcountActivity::class.java)
            intent.putExtra("role",role)
            startActivity(intent)
        }
        return binding.root
    }
    private fun getInfUser() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getAllUser(role).enqueue(object : Callback<ResultResponse<List<User>>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<List<User>>>,
                response: Response<ResultResponse<List<User>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { nestedList ->
                        listUser.clear()
                        nestedList.forEach { readings ->
                            listUser.addAll(readings)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<ResultResponse<List<User>>>, t: Throwable) {
                Log.d("Discover1", "${t.message}")
            }
        })
    }
}
