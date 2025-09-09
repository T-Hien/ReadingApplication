package vn.example.readingapplication.adapter.readbook

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.databinding.LayoutItemUserLikeBinding
import vn.example.readingapplication.fragment.readbook.ListLikeByUserFragment
import vn.example.readingapplication.model.Like
import java.text.SimpleDateFormat
import java.util.Locale

class ListBookLikesAdapter(private val listener: ListLikeByUserFragment,
                           private val itemList: MutableList<Like>) :
    RecyclerView.Adapter<ListBookLikesAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemUserLikeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = itemList[position]
        Log.d("LISTBOOK","$item")
        holder.binding.txtBookTitle.text = item.abook?.title.toString()
        holder.binding.txtGenreBook.text = "${holder.binding.txtGenreBook.text} ${item.abook?.listBookCategory?.joinToString { it.category?.name.toString() }}"
        holder.binding.txtAuthorBook.text = item.abook?.listDetailAuthor?.joinToString { it.author?.name.toString() }
        holder.binding.txtLikeBook.text = item.abook?.favorite?.number.toString()
        Glide.with(holder.itemView.context)
            .load(item.abook?.cover_image)
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)
        holder.binding.txtCreatedAt.text = "${holder.binding.txtCreatedAt.text} ${convertToDateTimeFormat(item.createdAt.toString())}"
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
            val bookId = item.abook?.id
            val intent = Intent(holder.itemView.context, ReadBookActivity::class.java).apply {
                putExtra("BOOK_ID", bookId)
            }
            holder.itemView.context.startActivity(intent)
        }

    }
    fun convertToDateTimeFormat(dateTime: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val targetFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        return try {
            val date = originalFormat.parse(dateTime)
            targetFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            "Invalid date format"
        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemUserLikeBinding) : RecyclerView.ViewHolder(binding.root)
}
