package vn.example.readingapplication.activity.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.adapter.admin.statics.CustomAuthorView
import vn.example.readingapplication.adapter.admin.statics.CustomBookView
import vn.example.readingapplication.adapter.admin.statics.CustomCategoryView
import vn.example.readingapplication.adapter.admin.statics.CustomeReadingView
import vn.example.readingapplication.databinding.ActivityAdminStatisticalBinding
import vn.example.readingapplication.fragment.admin.statics.StaticAuthorFragment
import vn.example.readingapplication.fragment.admin.statics.StaticBookFragment
import vn.example.readingapplication.fragment.admin.statics.StaticCategoryFragment
import vn.example.readingapplication.fragment.admin.statics.StaticReadingFragment
import vn.example.readingapplication.model.Favorite
import vn.example.readingapplication.model.statics.AuthorFavorite
import vn.example.readingapplication.model.statics.CategoryFavorite
import vn.example.readingapplication.model.statics.ReadingStatic
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class StatisticalActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAdminStatisticalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStatisticalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getListBook()
        getListAuthor()
        getListCategory()
        getListReading()

        getInforBook()
        getInforAuthor()
        getInforAdmin(1)
        getInforAdmin(2)
        getInforCategory()
        binding.btnAllAuthor.setOnClickListener(){
            val fragment = StaticAuthorFragment()
            replaceFragment(fragment)
        }
        binding.btnAllCategory.setOnClickListener(){
            val fragment = StaticCategoryFragment()
            replaceFragment(fragment)
        }
        binding.btnAllBook.setOnClickListener(){
            val fragment = StaticBookFragment()
            replaceFragment(fragment)
        }

        binding.btnExit.setOnClickListener(){
            finish()
        }
        binding.btnAllReading.setOnClickListener(){
            val fragment = StaticReadingFragment()
            replaceFragment(fragment)


        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fmView, fragment)
        binding.fmView.visibility = View.VISIBLE
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                binding.fmView.visibility = View.GONE
            }
        }
    }
    private fun getInforBook() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getCountBook().enqueue(object : Callback<ResultResponse<Int>> {
            override fun onResponse(call: Call<ResultResponse<Int>>, response: Response<ResultResponse<Int>>) {
                if (response.isSuccessful) {
                    val count = response.body()?.dataNum
                    binding.txtBook.text = "Sách: $count"

                } else {
                    Log.d("ERROR", "${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<ResultResponse<Int>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }
    private fun getInforCategory() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getCountCategory().enqueue(object : Callback<ResultResponse<Int>> {
            override fun onResponse(call: Call<ResultResponse<Int>>, response: Response<ResultResponse<Int>>) {
                if (response.isSuccessful) {
                    val count = response.body()?.dataNum
                    binding.txtCategory.text = "Thể loại: $count"

                } else {
                    Log.d("ERROR", "${response.errorBody()}")

                }
            }

            override fun onFailure(call: Call<ResultResponse<Int>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }
    private fun getInforAuthor() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getCountAuthor().enqueue(object : Callback<ResultResponse<Int>> {
            override fun onResponse(call: Call<ResultResponse<Int>>, response: Response<ResultResponse<Int>>) {
                if (response.isSuccessful) {
                    val count = response.body()?.dataNum
                    binding.txtAuthor.text = "Tác giả: $count"

                } else {
                    Log.d("ERROR", "${response.errorBody()}")

                }
            }

            override fun onFailure(call: Call<ResultResponse<Int>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }
    private fun getInforAdmin(role:Int) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getCountUserByRole(role).enqueue(object : Callback<ResultResponse<Int>> {
            override fun onResponse(call: Call<ResultResponse<Int>>, response: Response<ResultResponse<Int>>) {
                if (response.isSuccessful) {
                    val count = response.body()?.dataNum
                    if(role==1){
                        binding.txtStaff.text = "Quản lý: $count"
                    }
                    else{
                        binding.txtReader.text = "Đọc giả: $count"
                    }

                } else {
                    Log.d("ERROR", "${response.errorBody()}")

                }
            }

            override fun onFailure(call: Call<ResultResponse<Int>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
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
                    response.body()?.dataList?.let { categories ->
                        updateChart(categories)
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<AuthorFavorite>>, t: Throwable) {
                Toast.makeText(this@StatisticalActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateChart(categories: List<AuthorFavorite>) {
        val entries = categories.mapIndexed { index, category ->
            BarEntry(index.toFloat(), category.total_favorites.toFloat(), category)
        }

        val dataSet = BarDataSet(entries, "Tác giả")
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        dataSet.valueTextSize = 16f
        dataSet.valueFormatter = IntegerValueFormatter()

        val data = BarData(dataSet)
        binding.barChart.data = data

        // Thiết lập MarkerView tùy chỉnh
        val markerView = CustomAuthorView(this, R.layout.custom_marker_view)
        binding.barChart.marker = markerView

        binding.barChart.description.isEnabled = false
        binding.barChart.animateY(1000)
    }
    private fun getListCategory() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getStaticCategory().enqueue(object : Callback<ResultResponse<CategoryFavorite>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<CategoryFavorite>>,
                response: Response<ResultResponse<CategoryFavorite>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { categories ->
                        updateChartCategory(categories)
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<CategoryFavorite>>, t: Throwable) {
                Log.d("ERRO","${t.message}")
            }
        })
    }
    private fun getListReading() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getStaticReading().enqueue(object : Callback<ResultResponse<ReadingStatic>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<ReadingStatic>>,
                response: Response<ResultResponse<ReadingStatic>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { categories ->
                        Log.d("INFO_AUTHOR",categories.toString())
                        updateChartReading(categories)
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<ReadingStatic>>, t: Throwable) {
                Log.d("ERRO","${t.message}")
            }
        })
    }
    private fun getListBook() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getFavoriteBooks().enqueue(object : Callback<ResultResponse<Favorite>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Favorite>>,
                response: Response<ResultResponse<Favorite>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { books ->
                        updateChartBook(books)

                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Favorite>>, t: Throwable) {
                Log.d("ERROR:","Lastes:${t.message}")
            }
        })
    }
    private fun updateChartBook(categories: List<Favorite>) {
        val entries = categories.mapIndexed { index, category ->
            BarEntry(index.toFloat(), category.number.toFloat(), category)
        }

        val dataSet = BarDataSet(entries, "Sách")
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        dataSet.valueTextSize = 16f
        dataSet.valueFormatter = IntegerValueFormatter()

        val data = BarData(dataSet)
        binding.barChart0.data = data

        val markerView = CustomBookView(this, R.layout.custom_marker_view)
        binding.barChart0.marker = markerView

        binding.barChart0.description.isEnabled = false
        binding.barChart0.animateY(1000)
    }
    private fun updateChartCategory(categories: List<CategoryFavorite>) {
        val entries = categories.mapIndexed { index, category ->
            BarEntry(index.toFloat(), category.totalFavorites.toFloat(), category)
        }

        val dataSet = BarDataSet(entries, "Thể loại")
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        dataSet.valueTextSize = 16f
        dataSet.valueFormatter = IntegerValueFormatter()
        val data = BarData(dataSet)
        binding.barChart2.data = data

        // Thiết lập MarkerView tùy chỉnh
        val markerView = CustomCategoryView(this, R.layout.custom_marker_view)
        binding.barChart2.marker = markerView

        binding.barChart2.description.isEnabled = false
        binding.barChart2.animateY(1000)
    }
    class IntegerValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            // Định dạng số thành kiểu int mà không có dấu thập phân
            return value.toInt().toString()
        }
    }

    private fun updateChartReading(categories: List<ReadingStatic>) {
        val entries = categories.mapIndexed { index, category ->
            BarEntry(index.toFloat(), category.reading_count.toFloat(), category)
        }
        val dataSet = BarDataSet(entries, "Đọc giả")
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        dataSet.valueTextSize = 16f
        dataSet.valueFormatter = IntegerValueFormatter()

        val data = BarData(dataSet)
        binding.barChart4.data = data

        // Thiết lập MarkerView tùy chỉnh
        val markerView = CustomeReadingView(this, R.layout.custom_marker_view)
        binding.barChart4.marker = markerView

        binding.barChart4.description.isEnabled = false
        binding.barChart4.animateY(1000)
    }
}