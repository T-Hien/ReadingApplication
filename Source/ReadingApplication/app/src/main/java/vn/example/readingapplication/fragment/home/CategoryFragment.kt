package vn.example.readingapplication.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.activity.user.category.CategoryHomeActivity
import vn.example.readingapplication.adapter.home.CategoriesAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentHomeCategoriesBinding
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class CategoryFragment : BaseFragment(),CategoriesAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeCategoriesBinding
    private lateinit var categoryadapter: CategoriesAdapter
    private val categoryList = mutableListOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeCategoriesBinding.inflate(inflater, container, false)

        categoryadapter = CategoriesAdapter(categoryList, this)
        binding.rcvCategory.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rcvCategory.adapter = categoryadapter

        getListCategories()
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        Log.d("CategoryFragment", "Fragment resumed, reloading data...")
        reloadData()
    }
    private fun reloadData() {
        getListCategories()
    }

    private fun getListCategories() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getListCategory().enqueue(object : Callback<ResultResponse<Category>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Category>>,
                response: Response<ResultResponse<Category>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { books ->
                        categoryList.clear()
                        categoryList.addAll(books)
                        categoryadapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Category>>, t: Throwable) {
                Log.d("ERRO","Cate:${t.message}")
            }
        })
    }

    override fun onItemClick(category: Category) {
        val categoryId = category.id
        val intent = Intent(requireContext(), CategoryHomeActivity::class.java)
        intent.putExtra("CATEGORY_ID", categoryId)
        intent.putExtra("CATEGORY_NAME", category.name)
        intent.putExtra("CATEGORY_DESCRIPTION", category.description)
        startActivity(intent)
    }
}
