package vn.example.readingapplication.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemBooks1Binding
import vn.example.readingapplication.fragment.home.DiscoverFragment
import vn.example.readingapplication.model.BookCategory

class ListGenresBook1Adapter(private val itemList: MutableList<BookCategory>,
                             private val listener: DiscoverFragment
) :
    RecyclerView.Adapter<ListGenresBook1Adapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(bookCategory: BookCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemBooks1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val bookCategory = itemList[position]
        val context = holder.itemView.context
        val imageUrl:String? = bookCategory.abook?.cover_image
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)


        holder.binding.txtGenresBook.text = bookCategory.category?.name ?: "Unknown Category"
        holder.binding.txtBookName.text = bookCategory.abook?.title?: "Unknown Title"
        holder.itemView.setOnClickListener {
            listener.onItemClick(bookCategory)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemBooks1Binding) : RecyclerView.ViewHolder(binding.root)
}
