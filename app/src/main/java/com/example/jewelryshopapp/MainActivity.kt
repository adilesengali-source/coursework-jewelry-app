package com.example.jewelryshopapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnCatalog = findViewById<Button>(R.id.btnCatalog)
        val btnCalc = findViewById<Button>(R.id.btnCalc)

        btnCatalog.setOnClickListener {
            val intent = Intent(this, CatalogScreen::class.java)
            startActivity(intent)
        }

        btnCalc.setOnClickListener {
            val intent = Intent(this, CalcScreen::class.java)
            startActivity(intent)
        }
    }
}
