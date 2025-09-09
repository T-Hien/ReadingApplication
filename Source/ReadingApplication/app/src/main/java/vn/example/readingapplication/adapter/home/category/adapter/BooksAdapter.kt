package vn.example.readingapplication.adapter.home.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.CategoriesActivity
import vn.example.readingapplication.databinding.LayoutItemBooks1Binding
import vn.example.readingapplication.model.Book

class BooksAdapter(private val context: Context,
                   private val bookList: List<Book>,
                   private val listener: CategoriesActivity
) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = LayoutItemBooks1Binding.inflate(LayoutInflater.from(context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.binding.txtBookName.text = book.title

        Glide.with(context)
            .load(book.cover_image)
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)
        if(book.listBookCategory?.joinToString { it.category?.name.toString() }?.isNotEmpty() == true){
            holder.binding.txtGenresBook.text = book.listBookCategory?.joinToString { it.category?.name.toString() }
        }
        else{
            holder.binding.txtGenresBook.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(book)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class BookViewHolder(val binding: LayoutItemBooks1Binding) : RecyclerView.ViewHolder(binding.root)
}
