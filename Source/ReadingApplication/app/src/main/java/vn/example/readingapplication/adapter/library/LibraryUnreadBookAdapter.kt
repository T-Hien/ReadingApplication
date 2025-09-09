package vn.example.readingapplication.adapter.library

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.databinding.LayoutItemBooks1Binding
import vn.example.readingapplication.fragment.library.LibraryUnreadBookFragment
import vn.example.readingapplication.model.ReadingProgress

class LibraryUnreadBookAdapter(private val itemList: MutableList<ReadingProgress>,
                               private val listener: LibraryUnreadBookFragment,
                               private val context: Context
) :
    RecyclerView.Adapter<LibraryUnreadBookAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemBooks1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val readingProgress = itemList[position]
        Glide.with(holder.itemView.context)
            .load(readingProgress.abook?.cover_image)
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)
        val category = readingProgress.abook?.listBookCategory?.firstOrNull()?.category?.name ?: "N/A"
        holder.binding.txtGenresBook.text = category
        holder.binding.txtBookName.text = readingProgress.abook?.title
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
            val bookId = readingProgress.abook?.id
            val intent = Intent(context, ReadBookActivity::class.java).apply {
                putExtra("BOOK_ID", bookId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemBooks1Binding) : RecyclerView.ViewHolder(binding.root)
}
