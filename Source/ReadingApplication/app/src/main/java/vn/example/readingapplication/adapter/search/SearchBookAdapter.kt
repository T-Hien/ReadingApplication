package vn.example.readingapplication.adapter.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutItemSearchBooksBinding
import vn.example.readingapplication.fragment.search.SearchBookFragment
import vn.example.readingapplication.model.Search

class SearchBookAdapter(private val itemList: MutableList<Search>,
                        private val listener: SearchBookFragment) :
    RecyclerView.Adapter<SearchBookAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(search: Search)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemSearchBooksBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val search = itemList[position]
        holder.binding.txtBookSearch.text = search.keyword
        holder.itemView.setOnClickListener {
            listener.onItemClick(search)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemSearchBooksBinding) : RecyclerView.ViewHolder(binding.root)
}
