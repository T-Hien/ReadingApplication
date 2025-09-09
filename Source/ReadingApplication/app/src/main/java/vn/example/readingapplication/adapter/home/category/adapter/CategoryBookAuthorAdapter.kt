package vn.example.readingapplication.adapter.home.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.bookauthor.BookAuthorHomeActivity
import vn.example.readingapplication.databinding.LayoutItemBooks1Binding
import vn.example.readingapplication.model.Book

class CategoryBookAuthorAdapter(
    private val itemList: MutableList<Book>,
    private val listener: BookAuthorHomeActivity
) : RecyclerView.Adapter<CategoryBookAuthorAdapter.CardViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(book: Book)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemBooks1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val book = itemList[position]
        val imageUrl:String? = book.cover_image
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)


        holder.binding.txtGenresBook.text = book.listBookCategory?.firstOrNull()?.category?.name ?: ""
        holder.binding.txtBookName.text = book.title
        holder.itemView.setOnClickListener {
            listener.onItemClick(book)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemBooks1Binding) : RecyclerView.ViewHolder(binding.root)
}
