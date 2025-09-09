package vn.example.readingapplication.adapter.admin.book.update

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.activity.admin.book.UpdateBookActivity
import vn.example.readingapplication.databinding.LayoutAdminAddbookItemCategoryBinding
import vn.example.readingapplication.model.Category

class UpdateCategoriesAdapter(
    private val itemList: MutableList<Category>,
    private val onItemUpdateListener: UpdateBookActivity
) : RecyclerView.Adapter<UpdateCategoriesAdapter.CardViewHolder>() {
    private val list: MutableList<Int> = mutableListOf()

    interface OnItemUpdateListener {
        fun onItemUpdated(data: List<Int?>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutAdminAddbookItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val category = itemList[position]
        holder.binding.txtCategoryName.text = category.name // Giả sử 'name' là một thuộc tính của Category
        Log.d("AddCategoriesAdapter", "Binding category: ${category.id} at position $position")
    holder.binding.btnDeleteCategory.setOnClickListener {
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size)
        }
        itemList.map { it.id}?.let { onItemUpdateListener.onItemUpdated(it) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutAdminAddbookItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}
