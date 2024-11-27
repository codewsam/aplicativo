package com.example.sapatos_app

import ProdutoAdapter
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sapatos_app.Produto


class ProdutosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ProdutoAdapter
    private lateinit var produtos: MutableList<Produto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos)

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)

        // Inicializar dados
        produtos = mutableListOf(
            Produto("Imagem 1", R.drawable.girassollogo),
            Produto("tênis vermelho", R.drawable.tenis_vermelho),
            Produto("All star", R.drawable.allstar)
        )

        adapter = ProdutoAdapter(produtos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Lógica de pesquisa
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = produtos.filter {
                    it.nome.contains(newText ?: "", ignoreCase = true)
                }
                adapter = ProdutoAdapter(filteredList)
                recyclerView.adapter = adapter
                return true
            }
        })
    }
}
