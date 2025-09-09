package vn.example.readingapplication.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemRecentlybooksBinding
import vn.example.readingapplication.fragment.home.DiscoverFragment
import vn.example.readingapplication.model.ReadingProgress

class RecentlyBooksAdapter(private val itemList: List<ReadingProgress>,
                           private val listener: DiscoverFragment
) :
    RecyclerView.Adapter<RecentlyBooksAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(bookmark: ReadingProgress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemRecentlybooksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val bookmark = itemList[position]

        val imageUrl:String? = bookmark.abook?.cover_image
        Glide.with(holder.itemView.context)
            .load(imageUrl) // Load ảnh từ URL
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)

        holder.binding.txtBook.text = bookmark.abook?.title ?: "Unknown Title"
        holder.itemView.setOnClickListener {
            listener.onItemClick(bookmark)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemRecentlybooksBinding) : RecyclerView.ViewHolder(binding.root)
}
