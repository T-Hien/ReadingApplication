package vn.example.readingapplication.adapter.readbook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutItemReadbookLikeBinding
import vn.example.readingapplication.model.Like

class LikesAdapter(private val itemList: MutableList<Like>) :
    RecyclerView.Adapter<LikesAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemReadbookLikeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.txtName.text = item.auser?.name


    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemReadbookLikeBinding) : RecyclerView.ViewHolder(binding.root)
}
