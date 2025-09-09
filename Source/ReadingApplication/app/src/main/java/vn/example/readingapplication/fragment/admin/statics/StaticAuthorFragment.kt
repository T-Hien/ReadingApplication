package vn.example.readingapplication.fragment.admin.statics

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.adapter.admin.statics.StaticAuthorAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentAdminStaticAuthorBinding
import vn.example.readingapplication.model.statics.AuthorFavorite
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class StaticAuthorFragment : BaseFragment(){

    private lateinit var binding: FragmentAdminStaticAuthorBinding
    private lateinit var adapter: StaticAuthorAdapter
    private val itemList = mutableListOf<AuthorFavorite>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminStaticAuthorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StaticAuthorAdapter(itemList,this)
        binding.btnBack.setOnClickListener(){
            parentFragmentManager.popBackStack()
        }
        if (isAdded) {
            val layoutManager = FlexboxLayoutManager(requireActivity()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            binding.rcvSearchBook.layoutManager = layoutManager
            binding.rcvSearchBook.adapter = adapter
        }
        getListAuthor()
    }

    private fun getListAuthor() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getStaticAuthor().enqueue(object : Callback<ResultResponse<AuthorFavorite>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<AuthorFavorite>>,
                response: Response<ResultResponse<AuthorFavorite>>
            ) {
                if (response.isSuccessful) {
                    itemList.clear()
                    response.body()?.dataList?.let { categories ->
                        itemList.addAll(categories)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResultResponse<AuthorFavorite>>, t: Throwable) {
                Toast.makeText(context, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}