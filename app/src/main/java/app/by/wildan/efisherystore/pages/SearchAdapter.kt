package app.by.wildan.efisherystore.pages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import app.by.wildan.efisherystore.R
import kotlinx.android.synthetic.main.item_suggestion.view.*

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items: MutableList<String> = mutableListOf()
    private var listener : AdapterView.OnItemClickListener? = null

    fun notifyDataSetChanged(refreshData: List<String>) {
        items.apply {
            clear()
            addAll(refreshData)
            notifyDataSetChanged()
        }
    }

    fun addOnItemClickListener(continuation: (String)->Unit){
        listener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            continuation(items[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_suggestion, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            with(holder) {
                bind(items[position])
                itemView.setOnClickListener {
                    listener?.onItemClick(null,it,position,0L)
                }
            }
        }
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) = with(itemView) {
            itemSearchText.text = HtmlCompat.fromHtml(item, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }
}