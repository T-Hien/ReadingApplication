package vn.example.readingapplication.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutItemSearchBooksBinding
import vn.example.readingapplication.fragment.search.SearchCategoriesFragment
import vn.example.readingapplication.model.Search

class SearchCategoriesAdapter(private val itemList: MutableList<Search>,
                              private val listener: SearchCategoriesFragment
) :
    RecyclerView.Adapter<SearchCategoriesAdapter.CardViewHolder>() {
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
