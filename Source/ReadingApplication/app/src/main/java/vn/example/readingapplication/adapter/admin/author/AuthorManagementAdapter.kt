package vn.example.readingapplication.adapter.admin.author

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
import vn.example.readingapplication.activity.admin.author.ShowBookListDialog
import vn.example.readingapplication.activity.admin.category.UpdateCategoryActivity
import vn.example.readingapplication.adapter.admin.category.CategoryManagementAdapter.CardViewHolder
import vn.example.readingapplication.databinding.LayoutItemAdminCategoryBinding
import vn.example.readingapplication.model.Author
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class AuthorManagementAdapter(private val itemList: MutableList<Author>
) :
    RecyclerView.Adapter<AuthorManagementAdapter.CardViewHolder>(){
//    interface OnItemClickListener {
//        fun onItemClick(cate: Category)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemAdminCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = itemList[position]
        Log.d("Adapter", "Categories: ${item}")
        holder.binding.txtCategory.text = item.name
        holder.binding.btnEditCategory.setOnClickListener() {
            val intent = Intent(holder.itemView.context, UpdateCategoryActivity::class.java).apply {
                putExtra("P", 1)
                putExtra("ID", item.id)
                putExtra("NAME", item.name)
                putExtra("BIRTH", item.birth_date)
                putExtra("DEATH", item.death_date)

//                putExtra("CATEGORY_DESCRIPTION", item.description)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.binding.btnDeleteCategory.setOnClickListener(){
            item.id?.let { categoryId ->
                checkBooksInAuthor(categoryId, item, holder)
            }


        }

    }
    private fun checkBooksInAuthor(authorId: Int, author: Author, holder: vn.example.readingapplication.adapter.admin.author.AuthorManagementAdapter.CardViewHolder) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getAuthorListBookAdmin(authorId).enqueue(object :
            Callback<ResultResponse<Book>> {
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
                        val deleteDialog = ShowDeleteDialog.newInstance2(
                            2,
                            author.id ?: 0,
                            "A"
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
