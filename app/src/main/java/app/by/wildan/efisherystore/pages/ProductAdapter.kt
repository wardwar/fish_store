package app.by.wildan.efisherystore.pages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.by.wildan.efisherystore.R
import app.by.wildan.efisherystore.data.entity.Product
import app.by.wildan.efisherystore.data.entity.priceGram
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_product.view.*

class ProductAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val items: MutableList<Product> = mutableListOf()

    fun notifyDataSetChanged(refreshData : List<Product>){
        items.apply {
            clear()
            addAll(refreshData)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ProductViewHolder){
            with(holder){
                    bind(items[position])
            }
        }
    }

    internal class ProductViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: Product) = with(itemView){
            val fishImage = listOf(
                R.drawable.udang,
                R.drawable.nila
            )
            Picasso.get().load(fishImage.random()).into(itemProductImage)
            itemProductNameText.text = item.komoditas
            itemProductPriceText.text = item.priceGram
            itemProductProvinsiText.text = item.area_provinsi?.capitalize()
        }
    }
}