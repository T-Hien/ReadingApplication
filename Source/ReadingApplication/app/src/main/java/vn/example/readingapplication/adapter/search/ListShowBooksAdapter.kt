package vn.example.readingapplication.adapter.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemSearchBookBinding
import vn.example.readingapplication.fragment.SearchFragment
import vn.example.readingapplication.model.search.BookSearch

class ListShowBooksAdapter(private val listener: SearchFragment) : ListAdapter<BookSearch,
ListShowBooksAdapter.CardViewHolder>(DiffCallback()) {
    // DiffUtil để so sánh hai item trong danh sách
    interface OnItemClickListener {
        fun onItemClick(bookSearch: BookSearch)
    }
    class DiffCallback : DiffUtil.ItemCallback<BookSearch>() {
        override fun areItemsTheSame(oldItem: BookSearch, newItem: BookSearch): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookSearch, newItem: BookSearch): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemSearchBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val book = getItem(position)
        Glide.with(holder.itemView.context)
            .load(book.cover_image)
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)
        Log.d("SEARCH_FRAGMENT","BOOK: $book")
        holder.binding.txtBookTitle.text = book.title
        val authors = book.listDetailAuthor?.joinToString { it.author?.name.toString() }
        holder.binding.txtAuthorBook.text = holder.itemView.context.getString(R.string.text_author_name) + " $authors"
        holder.binding.txtLikeBook.text = book.favorite?.number.toString()
        val cate = book.listBookCategory?.joinToString { it.category?.name.toString() }
        holder.binding.txtGenreBook.text = holder.itemView.context.getString(R.string.text_category_name) + "$cate"
        holder.itemView.setOnClickListener {
            listener.onItemClick(book)
        }
    }

    class CardViewHolder(val binding: LayoutItemSearchBookBinding) : RecyclerView.ViewHolder(binding.root)
}
