package vn.example.readingapplication.adapter.admin.book.add

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.activity.admin.book.AddBookActivity
import vn.example.readingapplication.databinding.LayoutAdminAddbookItemCategoryBinding
import vn.example.readingapplication.model.Category

class AddCategoriesAdapter(
    private val itemList: MutableList<Category>,
    private val onItemUpdateListener: AddBookActivity
) : RecyclerView.Adapter<AddCategoriesAdapter.CardViewHolder>() {

    interface OnItemUpdateListener {
        fun onItemUpdated(data: List<Int?>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutAdminAddbookItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val category = itemList[position]
        holder.binding.txtCategoryName.text = category.name // Assuming 'name' is a property of Category
        Log.d("AddCategoriesAdapter", "Binding category: ${category.name} at position $position")

        holder.binding.btnDeleteCategory.setOnClickListener {
            // Xóa danh mục khỏi danh sách
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
