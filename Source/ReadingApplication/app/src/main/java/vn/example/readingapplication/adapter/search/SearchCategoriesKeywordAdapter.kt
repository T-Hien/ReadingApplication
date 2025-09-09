package vn.example.readingapplication.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutItemSearchCategoryBinding
import vn.example.readingapplication.fragment.search.SearchCategoriesFragment
import vn.example.readingapplication.model.Category

class SearchCategoriesKeywordAdapter(private val itemList: MutableList<Category>,
                                     private val listener: SearchCategoriesFragment
) :
    RecyclerView.Adapter<SearchCategoriesKeywordAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemSearchCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val category = itemList[position]
        holder.binding.txtCategoryName.text = category.name
        holder.itemView.setOnClickListener {
            listener.onItemClick(category)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemSearchCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}
