package vn.example.readingapplication.adapter.search

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemSearchCategoryBinding
import vn.example.readingapplication.fragment.search.SearchAuthorFragment
import vn.example.readingapplication.fragment.search.SearchBookFragment
import vn.example.readingapplication.model.Author
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Search

class SearchAuthorKeywordAdapter(private val itemList: MutableList<Author>,
                                 private val listener: SearchAuthorFragment) :
    RecyclerView.Adapter<SearchAuthorKeywordAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(au: Author)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemSearchCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val au = itemList[position]
        Log.d("INF_AUKEY","$au")
       holder.binding.txtCategoryName.text = au.name
        holder.itemView.setOnClickListener {
            listener.onItemClick(au)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemSearchCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}
