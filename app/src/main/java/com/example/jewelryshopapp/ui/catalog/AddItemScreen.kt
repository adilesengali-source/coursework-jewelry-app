package com.example.jewelryshopapp.ui.catalog

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jewelryshopapp.R
import com.example.jewelryshopapp.data.AppDatabase
import com.example.jewelryshopapp.data.Item
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.enableEdgeToEdge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddItemScreen : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.add_item_screen)

        // Настраиваем стрелку назад в ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Добавить изделие"

        db = AppDatabase.getDatabase(this)

        val inputName = findViewById<TextInputEditText>(R.id.inputName)
        val inputType = findViewById<TextInputEditText>(R.id.inputType)
        val inputPrice = findViewById<TextInputEditText>(R.id.inputPrice)
        val btnSave = findViewById<MaterialButton>(R.id.btnSaveItem)

        btnSave.setOnClickListener {
            val name = inputName.text.toString().trim()
            val type = inputType.text.toString().trim()
            val priceText = inputPrice.text.toString().trim()

            if (name.isBlank() || type.isBlank() || priceText.isBlank()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceText.toDoubleOrNull()
            if (price == null || price <= 0) {
                Toast.makeText(this, "Введите корректную цену", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newItem = Item(name = name, type = type, price = price)

            lifecycleScope.launch(Dispatchers.IO) {
                db.itemDao().insertItem(newItem)
                finish() // вернуться на предыдущий экран после сохранения
            }
        }
    }

    // Обрабатываем нажатие на стрелку «назад» в ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // вернуться на предыдущий экран
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
