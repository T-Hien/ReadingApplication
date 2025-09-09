package vn.example.readingapplication.adapter.admin.statics

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutAdminItemStaticBinding
import vn.example.readingapplication.model.Like
import java.text.SimpleDateFormat
import java.util.Locale

class StatisticAdapter(private val statistics: List<Like>) :
    RecyclerView.Adapter<StatisticAdapter.CardViewHolder>() {
    fun convertDateTimeString(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(input)
        return outputFormat.format(date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutAdminItemStaticBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = statistics[position]
        val output = convertDateTimeString(item.abook?.createdAt.toString())
        Log.d("INF_STATIC_1", output)
        holder.binding.bookId.text = output
        holder.binding.categoryName.text = item.abook?.title.toString()
        holder.binding.bookCount.text = item.auser?.name.toString()
    }
    override fun getItemCount(): Int {
        return statistics.size
    }

    class CardViewHolder(val binding: LayoutAdminItemStaticBinding) : RecyclerView.ViewHolder(binding.root)
}
