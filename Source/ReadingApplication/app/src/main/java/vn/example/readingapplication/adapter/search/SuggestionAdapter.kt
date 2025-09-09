package vn.example.readingapplication.adapter.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.R

class SuggestionAdapter(
    private val suggestions: List<String>, // Dữ liệu luôn đồng bộ với suggestionList
    private val onClick: (String) -> Unit // Hàm xử lý khi chọn gợi ý
) : RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder>() {

    inner class SuggestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtSuggestion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_suggestion, parent, false)
        return SuggestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.textView.text = suggestion
        holder.textView.setOnClickListener { onClick(suggestion) }
    }

    override fun getItemCount(): Int {
        return suggestions.size
    }
}
