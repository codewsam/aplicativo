package com.example.sapatos_app

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity

class produtosActivity : AppCompatActivity() {

    private lateinit var searchBar: EditText
    private lateinit var searchIcon: ImageView
    private lateinit var listView: ListView

    // Lista simulada de produtos
    private val productList = listOf(
        "Tênis Esportivo",
        "Bota de Couro",
        "Sapato Social",
        "Sandália Confortável",
        "Chinelo Casual"
    )

    // Lista filtrada
    private var filteredList = productList.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos)

        // Inicializando os componentes


        // Configurar o adapter do ListView
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            filteredList
        )
        listView.adapter = adapter

        // Configurando a lógica de pesquisa
        searchIcon.setOnClickListener {
            val query = searchBar.text.toString()
            filteredList = productList.filter { it.contains(query, ignoreCase = true) }.toMutableList()
            adapter.clear()
            adapter.addAll(filteredList)
            adapter.notifyDataSetChanged()
        }
    }
}
