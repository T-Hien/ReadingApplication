package vn.example.readingapplication.adapter.reading.list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemReadingListBookmarkBinding
import vn.example.readingapplication.fragment.reading.list.BookmarkFragment
import vn.example.readingapplication.model.Bookmarks
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class BookmarkAdapter(
    private val itemList: MutableList<Bookmarks>,
    private val listener: BookmarkFragment
) : RecyclerView.Adapter<BookmarkAdapter.CardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(bookmark: Bookmarks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemReadingListBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val bookmark = itemList[position]
        val roundedNumber = String.format("%.1f", bookmark.progress_percentage?.toFloat())
        holder.binding.txtPosition.text = holder.itemView.context.getString(R.string.text_position)+" ${bookmark.position}"
        holder.binding.txtPercent.text = "$roundedNumber%"
        holder.binding.txtTime.text = bookmark.createdAt
        holder.binding.btnDelete.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {

                holder.binding.btnDelete.isEnabled = false

                bookmark.id?.let { it1 -> deleteBookmark(it1, holder.adapterPosition) }
                holder.binding.btnDelete.isEnabled = true
            }

        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(bookmark)
        }
    }
    private fun deleteBookmark(bookId: Int, position: Int) {
        if (position >= itemList.size || position < 0) {
            Log.e("DELETE", "Invalid position: $position")
            return
        }

        val id: Int? = bookId
        if (id == null) {
            Log.e("DELETE", "Invalid ID: $id")
            return
        }
        val requestBody = mapOf("id" to id)
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteBookmarkById(requestBody).enqueue(object : Callback<ResultResponse<Void>> {
            override fun onResponse(
                call: Call<ResultResponse<Void>>,
                response: Response<ResultResponse<Void>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {

                        itemList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, itemList.size) // Cập nhật các vị trí còn lại
                    }
                    else if (response.code() == 404) {
                        Log.e("DELETE", "Bookmark not found on server.")
                    }
                }
            }
            override fun onFailure(call: Call<ResultResponse<Void>>, t: Throwable) {
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemReadingListBookmarkBinding) : RecyclerView.ViewHolder(binding.root)
}
