package vn.example.readingapplication.activity.admin.statics

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.adapter.admin.statics.StatisticAdapter
import vn.example.readingapplication.databinding.ActivityFavoriteStaticBinding
import vn.example.readingapplication.model.Like
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class FavoriteStaticActivity : AppCompatActivity() {

    private lateinit var adapter: StatisticAdapter
    private val itemList = mutableListOf<Like>()
    private lateinit var binding: ActivityFavoriteStaticBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteStaticBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with GridLayoutManager
        binding.rcvFavorite.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        adapter = StatisticAdapter(itemList)
        binding.rcvFavorite.adapter = adapter

        // Load data
        getAllLike()
    }

    private fun getAllLike() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.findAllLike().enqueue(object : Callback<ResultResponse<Like>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Like>>,
                response: Response<ResultResponse<Like>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { likes ->
                        itemList.clear()
                        itemList.addAll(likes)
                        Log.d("INF_STATIC_2", likes.toString())
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e("INF_STATIC_ERROR", "Response unsuccessful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResultResponse<Like>>, t: Throwable) {
                Log.e("INF_STATIC_ERROR", "API call failed: ${t.message}")
            }
        })
    }
}
