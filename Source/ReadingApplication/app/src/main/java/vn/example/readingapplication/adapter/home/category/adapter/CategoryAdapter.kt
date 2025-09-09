package vn.example.readingapplication.adapter.home.category.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.category.CategoryHomeActivity
import vn.example.readingapplication.databinding.LayoutHomeAllCategoryBinding
import vn.example.readingapplication.databinding.LayoutItemBooks1Binding
import vn.example.readingapplication.fragment.home.DiscoverFragment
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.BookCategory
import vn.example.readingapplication.model.Category

class CategoryAdapter(
    private val itemList: List<BookCategory>,
    private val listener: CategoryHomeActivity
) : RecyclerView.Adapter<CategoryAdapter.CardViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(book: BookCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemBooks1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val bookCategory = itemList[position]
        val imageUrl:String? = bookCategory.abook?.cover_image
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)


        holder.binding.txtGenresBook.text = bookCategory.category?.name ?: "Unknown Category"
        holder.binding.txtBookName.text = bookCategory.abook?.title ?: "Unknown Title"
        holder.itemView.setOnClickListener {
            listener.onItemClick(bookCategory)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemBooks1Binding) : RecyclerView.ViewHolder(binding.root)
}
