package com.example.jewelryshopapp.ui.catalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jewelryshopapp.R
import com.example.jewelryshopapp.data.Item

class CatalogAdapterGrid(
    private var items: List<Item>,
    private val onItemClick: (Item) -> Unit,
    private val onItemDeleteClick: (Item) -> Unit   // кнопка удаления
) : RecyclerView.Adapter<CatalogAdapterGrid.ProductVH>() {

    class ProductVH(view: View) : RecyclerView.ViewHolder(view) {
        val itemEmoji: TextView = view.findViewById(R.id.itemEmoji)
        val name: TextView = view.findViewById(R.id.itemName)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_grid, parent, false)
        return ProductVH(view)
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.price.text = holder.itemView.context.getString(R.string.item_price, item.price)

        // Эмодзи по типу изделия
        holder.itemEmoji.text = when (item.type.lowercase()) {
            "кольцо" -> "💍"
            "браслет" -> "📿"
            "серьги" -> "👂"
            else -> "❓"
        }

        // Переход в детали
        holder.itemView.setOnClickListener { onItemClick(item) }

        // 🔴 Кнопка удаления
        holder.btnDelete.setOnClickListener { onItemDeleteClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<Item>) {
        val diffCallback = ItemDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    private class ItemDiffCallback(
        private val oldList: List<Item>,
        private val newList: List<Item>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.name == newItem.name &&
                    oldItem.type == newItem.type &&
                    oldItem.price == newItem.price
        }
    }
}
