package vn.example.readingapplication.adapter.home.category.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.activity.user.CategoriesActivity
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.activity.user.category.CategoryHomeActivity
import vn.example.readingapplication.databinding.LayoutHomeAllCategoryBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Category

class ListCategoryAdapter(
    private val categoryList: MutableList<Category>,
    private val listener: CategoriesActivity,
    private val bookMap: MutableMap<Category, MutableList<Book>>
) : RecyclerView.Adapter<ListCategoryAdapter.CardViewHolder>() {

    init {
        Log.d("ListCategoryAdapter", "Adapter initialized with categories: $categoryList")
    }

    interface OnItemClickListener {
        fun onItemClick(book: Book)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutHomeAllCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        Log.d("ListCategoryAdapter", "Binding category")

        val category = categoryList[position]
        val books = bookMap[category] ?: emptyList()

        holder.binding.txtCategoryType.text = category.name
        holder.binding.btnSee.setOnClickListener {
            val intent = Intent(holder.itemView.context, CategoryHomeActivity::class.java)
            intent.putExtra("CATEGORY_ID", category.id)
            intent.putExtra("CATEGORY_NAME", category.name)
            intent.putExtra("CATEGORY_DESCRIPTION", category.description)
            holder.itemView.context.startActivity(intent)
        }

        if (books.isNotEmpty()) {
            val layoutManager = GridLayoutManager(holder.itemView.context, 3)
            holder.binding.rvListCategory.layoutManager = layoutManager
            val adapter = BooksAdapter(holder.itemView.context, books, listener)
            holder.binding.rvListCategory.adapter = adapter
            holder.binding.rvListCategory.post {
                val itemHeight = 530
                val numberOfRows = if (books.size <= 3) 1 else 2
                val totalHeight = itemHeight * numberOfRows
                holder.binding.rvListCategory.layoutParams.height = totalHeight
            }
            holder.binding.rvListCategory.visibility = View.VISIBLE
        } else
        {
            holder.binding.rvListCategory.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class CardViewHolder(val binding: LayoutHomeAllCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}
