package vn.example.readingapplication.adapter.reading.list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemReadingListBookmarkBinding
import vn.example.readingapplication.fragment.reading.list.NoteFragment
import vn.example.readingapplication.model.Bookmarks
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class NoteAdapter(private val itemList: MutableList<Bookmarks>,
                  private val listener: NoteFragment,
) : RecyclerView.Adapter<NoteAdapter.CardViewHolder>() {

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
        holder.binding.txtContent.visibility = View.VISIBLE
        holder.binding.txtContent.text = bookmark.note?.content
        holder.binding.btnDelete.setOnClickListener {
            Log.d("DELETE","${bookmark.id}, $position")
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                holder.binding.btnDelete.isEnabled = false
                bookmark.id?.let { id -> deleteNote(id, adapterPosition) }
                holder.binding.btnDelete.isEnabled = true

            }
        }
        holder.itemView.setOnClickListener {
            Log.d("NOTE_INFOR:","NOTEADAPTER_DATA: ${bookmark}")
            listener.onItemClick(bookmark)
        }
    }
    private fun deleteNote(noteId: Int, position: Int) {
        if (position < 0 || position >= itemList.size) {
            Log.e("ERROR", "Invalid position: $position for list size: ${itemList.size}")
            return
        }

        val id = noteId
        if (id == null) {
            return
        }
        val requestBody = mapOf("id" to id)
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteNoteByNoteId(requestBody).enqueue(object : Callback<ResultResponse<Void>> {
            override fun onResponse(
                call: Call<ResultResponse<Void>>,
                response: Response<ResultResponse<Void>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
                        itemList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, itemList.size)
                    }
                }
            }
            override fun onFailure(call: Call<ResultResponse<Void>>, t: Throwable) {
                Log.d("ERRORNOTE: ", "${t.message}")
            }
        })
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemReadingListBookmarkBinding) : RecyclerView.ViewHolder(binding.root)
}
