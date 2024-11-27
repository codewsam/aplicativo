package com.example.sapatos_app


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnIrPedidos: Button = findViewById(R.id.btnPedidos)




        btnIrPedidos.setOnClickListener {

            val intent = Intent(this, PedidosActivity::class.java)
            startActivity(intent)
        }
        val btnProdutos: Button = findViewById(R.id.btnProdutos)

        // Abrir ProdutosActivity ao clicar
        btnProdutos.setOnClickListener {
            val intent = Intent(this, ProdutosActivity::class.java)
            startActivity(intent)
        }

    }
}