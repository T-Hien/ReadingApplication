package vn.example.readingapplication.adapter.reading

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.FragmentReadingSettingBinding
import vn.example.readingapplication.model.Setting

class ReadingModeAdapter(private val itemList: MutableList<Setting>) :
    RecyclerView.Adapter<ReadingModeAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = FragmentReadingSettingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
//        val note = itemList[position]
        }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: FragmentReadingSettingBinding) : RecyclerView.ViewHolder(binding.root)
}
