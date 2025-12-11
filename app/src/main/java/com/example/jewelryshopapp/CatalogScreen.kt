package com.example.jewelryshopapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jewelryshopapp.data.AppDatabase
import com.example.jewelryshopapp.data.Item
import com.example.jewelryshopapp.ui.catalog.AddItemScreen
import com.example.jewelryshopapp.ui.catalog.CatalogAdapterGrid
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.appcompat.app.AlertDialog

class CatalogScreen : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CatalogAdapterGrid
    private lateinit var db: AppDatabase
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.screen_catalog)

        db = AppDatabase.getDatabase(this)
        recyclerView = findViewById(R.id.catalogRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = CatalogAdapterGrid(
            items = emptyList(),
            onItemClick = { item ->
                val intent = Intent(this, DetailScreen::class.java)
                intent.putExtra(DetailScreen.EXTRA_NAME, item.name)
                intent.putExtra(DetailScreen.EXTRA_PRICE, item.price)
                intent.putExtra(DetailScreen.EXTRA_TYPE, item.type)
                startActivity(intent)
            },
            onItemDeleteClick = { item ->
                showDeleteDialog(item)
            }
        )

        recyclerView.adapter = adapter

        fabAdd = findViewById(R.id.fabAddItem)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddItemScreen::class.java))
        }

        loadItems()
    }

    override fun onResume() {
        super.onResume()
        loadItems()
    }

    private fun showDeleteDialog(item: Item) {
        AlertDialog.Builder(this)
            .setTitle("Удалить изделие")
            .setMessage("Вы действительно хотите удалить «${item.name}»?")
            .setPositiveButton("Да") { _, _ -> deleteItem(item) }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun deleteItem(item: Item) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) { db.itemDao().deleteItem(item) }
            loadItems()
        }
    }

    private fun loadItems() {
        lifecycleScope.launch {
            val items: List<Item> = withContext(Dispatchers.IO) { db.itemDao().getAllItems() }
            val emptyCatalogView = findViewById<View>(R.id.tvEmptyCatalog)
            emptyCatalogView.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            adapter.update(items)
        }
    }
}
