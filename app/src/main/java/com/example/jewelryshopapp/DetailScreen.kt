package com.example.jewelryshopapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jewelryshopapp.R

class DetailScreen : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PRICE = "extra_price"
        const val EXTRA_TYPE = "extra_type"
    }

    private lateinit var detailEmoji: TextView
    private lateinit var detailName: TextView
    private lateinit var detailType: TextView
    private lateinit var detailPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.screen_detail)

        // Настраиваем ActionBar с кнопкой "назад"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Детали изделия"

        // Инициализация элементов
        detailEmoji = findViewById(R.id.detailEmoji)
        detailName = findViewById(R.id.detailName)
        detailType = findViewById(R.id.detailType)
        detailPrice = findViewById(R.id.detailPrice)

        val name = intent.getStringExtra(EXTRA_NAME) ?: "—"
        val type = intent.getStringExtra(EXTRA_TYPE) ?: "—"
        val price = intent.getDoubleExtra(EXTRA_PRICE, 0.0)

        // Заполняем данные
        detailName.text = name
        detailType.text = type
        detailPrice.text = getString(R.string.item_price, price)

        // Эмодзи оставляем статическим (💍) или можно выбрать по типу изделия
        detailEmoji.text = when (type.lowercase()) {
            "кольцо" -> "💍"
            "браслет" -> "📿"
            "серьги" -> "🪄"
            else -> "💎"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
