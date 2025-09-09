package vn.example.readingapplication.adapter.admin.category

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.activity.admin.ShowDeleteDialog
import vn.example.readingapplication.activity.admin.category.ShowBookListDialog
import vn.example.readingapplication.activity.admin.category.UpdateCategoryActivity
import vn.example.readingapplication.adapter.home.category.adapter.CategoryBookAuthorAdapter
import vn.example.readingapplication.databinding.LayoutItemAdminCategoryBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class CategoryManagementAdapter(private val itemList: MutableList<Category>
) :
    RecyclerView.Adapter<CategoryManagementAdapter.CardViewHolder>() {
    private val bookList =mutableListOf<Book>()
    private lateinit var adapter: CategoryBookAuthorAdapter

//    interface OnItemClickListener {
//        fun onItemClick(cate: Category)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemAdminCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.txtCategory.text = item.name
        holder.binding.btnEditCategory.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateCategoryActivity::class.java).apply {
                putExtra("P", 2)
                putExtra("ID", item.id)
                putExtra("NAME", item.name)
                putExtra("DESCRIPTION", item.description)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.binding.btnDeleteCategory.setOnClickListener {
            // Gọi hàm kiểm tra sách thuộc thể loại
            item.id?.let { categoryId ->
                checkBooksInCategory(categoryId, item, holder)
            }
        }
    }

    private fun checkBooksInCategory(categoryId: Int, category: Category, holder: CardViewHolder) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getCategoryListBookAdmin(categoryId).enqueue(object : Callback<ResultResponse<Book>> {
            override fun onResponse(call: Call<ResultResponse<Book>>, response: Response<ResultResponse<Book>>) {
                if (response.isSuccessful) {
                    val books = response.body()?.dataList
                    Log.e("ShowDeleteDialog_DATA", "DATA_${books}")
                    if (!books.isNullOrEmpty()) {
                        // Nếu có sách, hiển thị dialog liệt kê tên sách
                        val bookNames = books.joinToString("\n") { it.title.toString() }
                        val dialog = ShowBookListDialog.newInstance(bookNames)
                        dialog.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "BookListDialog")
                    }
                }
                else {
                    // Nếu không có sách, hiển thị dialog xác nhận xóa thể loại
                    val activity = holder.itemView.context as? AppCompatActivity
                    if (activity != null) {
                        val deleteDialog = ShowDeleteDialog.newInstance(
                            3,
                            category.id ?: 0,
                            category.name ?: "",
                            category.description ?: ""
                        )
                        deleteDialog.show(activity.supportFragmentManager, "DeleteCategoryDialog")
                    } else {
                        Log.e("ShowDeleteDialog", "Context is not AppCompatActivity")
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                Log.e("Error", "Failed to fetch books: ${t.message}")
            }
        })
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemAdminCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}
