package com.example.jewelryshopapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jewelryshopapp.data.AppDatabase
import com.example.jewelryshopapp.data.Calculation
import com.example.jewelryshopapp.data.Item
import com.example.jewelryshopapp.databinding.ScreenCalcBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CalcScreen : AppCompatActivity() {

    private lateinit var binding: ScreenCalcBinding
    private lateinit var db: AppDatabase
    private var itemsList: List<Item> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ScreenCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        loadItems()
        setupListeners()
    }

    private fun loadItems() {
        lifecycleScope.launch {
            itemsList = withContext(Dispatchers.IO) {
                db.itemDao().getAllItems()
            }

            if (itemsList.isEmpty()) {
                binding.spinnerItem.visibility = View.GONE
                binding.btnCalculate.visibility = View.GONE
                binding.tvResult.text = "Нет изделий. Добавьте изделие сначала."
                return@launch
            }

            val adapter = ArrayAdapter(
                this@CalcScreen,
                android.R.layout.simple_spinner_item,
                itemsList.map { it.name }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerItem.adapter = adapter

            // Автоматическая подстановка цены при выборе изделия
            binding.spinnerItem.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = itemsList[position]
                    binding.etPrice.setText(selectedItem.price.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    binding.etPrice.setText("")
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnCalculate.setOnClickListener {
            val selectedIndex = binding.spinnerItem.selectedItemPosition
            if (selectedIndex < 0 || selectedIndex >= itemsList.size) {
                Toast.makeText(this, "Выберите изделие", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val priceText = binding.etPrice.text.toString().trim()
            val quantityText = binding.etQuantity.text.toString().trim()

            if (priceText.isEmpty() || quantityText.isEmpty()) {
                Toast.makeText(this, "Введите цену и количество", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceText.toDoubleOrNull()
            val quantity = quantityText.toIntOrNull()

            if (price == null || quantity == null) {
                Toast.makeText(this, "Введите корректные значения", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val itemName = itemsList[selectedIndex].name
            val total = price * quantity

            // Сохраняем расчёт в базу данных
            lifecycleScope.launch(Dispatchers.IO) {
                db.calculationDao().insertCalculation(
                    Calculation(
                        itemName = itemName,
                        price = price,
                        quantity = quantity,
                        total = total,
                        date = Date().time
                    )
                )
            }

            // Обновляем результат на экране
            binding.tvResult.text = getString(
                R.string.calc_result,
                itemName,
                total
            )
        }
    }
}
