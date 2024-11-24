package com.example.sapatos_app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.sapatos_app.data.Pedido
import com.example.sapatos_app.data.AppDatabase
import com.example.sapatos_app.data.PedidoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class PedidosActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: android.net.Uri? = null
    private lateinit var pedidoDao: PedidoDao // DAO do banco de dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        // Inicializar o banco de dados e DAO
        val database = AppDatabase.getDatabase(this)
        pedidoDao = database.pedidoDao()

        // Referência para elementos da interface
        val btnAddPedido: Button = findViewById(R.id.btnAdicionarPedido)
        val linearLayoutPedidos = findViewById<LinearLayout>(R.id.linearLayoutPedidos)
        val btnVoltar: Button = findViewById(R.id.btnVoltar)

        // Carregar pedidos do banco ao abrir a tela
        carregarPedidos(linearLayoutPedidos)

        // Ação do botão de adicionar pedido
        btnAddPedido.setOnClickListener {
            showAddPedidoDialog(linearLayoutPedidos)
        }

        // Ação do botão de voltar
        btnVoltar.setOnClickListener {
            finish()
        }
    }

    // Função para exibir o AlertDialog para adicionar um pedido
    @SuppressLint("MissingInflatedId")
    private fun showAddPedidoDialog(linearLayoutPedidos: LinearLayout) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_pedido, null)
        val nomeClienteEditText = dialogView.findViewById<EditText>(R.id.editNomeCliente)
        val dataPedidoDatePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
        val produtoEditText = dialogView.findViewById<EditText>(R.id.editProduto)
        val btnSelectImage: Button = dialogView.findViewById(R.id.btnSelectImage)

        btnSelectImage.setOnClickListener {
            openGallery()
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Adicionar Pedido")
            .setView(dialogView)
            .setPositiveButton("Adicionar") { _, _ ->
                val nomeCliente = nomeClienteEditText.text.toString()
                val dataPedido = "${dataPedidoDatePicker.dayOfMonth}/${dataPedidoDatePicker.month + 1}/${dataPedidoDatePicker.year}"
                val produto = produtoEditText.text.toString()

                if (nomeCliente.isNotEmpty() && produto.isNotEmpty()) {
                    val novoPedido = Pedido(
                        nomeCliente = nomeCliente,
                        dataPedido = dataPedido,
                        produto = produto,
                        imagemUri = imageUri?.toString()
                    )

                    // Inserir no banco de dados e atualizar a interface
                    CoroutineScope(Dispatchers.IO).launch {
                        pedidoDao.inserirPedido(novoPedido)
                        withContext(Dispatchers.Main) {
                            val pedidoCard = createPedidoCard(novoPedido)
                            linearLayoutPedidos.addView(pedidoCard)
                        }
                    }
                } else {
                    Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    // Função para criar o CardView com as informações do pedido
    private fun createPedidoCard(pedido: Pedido): CardView {
        val cardView = LayoutInflater.from(this).inflate(R.layout.item_pedido, null) as CardView
        val textNomeCliente = cardView.findViewById<TextView>(R.id.textNomeCliente)
        val textDataPedido = cardView.findViewById<TextView>(R.id.textDataPedido)
        val textProduto = cardView.findViewById<TextView>(R.id.textProduto)
        val btnExcluir = cardView.findViewById<Button>(R.id.btnExcluir)
        val imageView = cardView.findViewById<ImageView>(R.id.imagePedido)

        textNomeCliente.text = "Cliente: ${pedido.nomeCliente}"
        textDataPedido.text = "Data: ${pedido.dataPedido}"
        textProduto.text = "Produto: ${pedido.produto}"

        if (pedido.imagemUri != null) {
            imageView.setImageURI(android.net.Uri.parse(pedido.imagemUri))
        }

        btnExcluir.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                pedidoDao.deletarPedido(pedido.id)
                withContext(Dispatchers.Main) {
                    (cardView.parent as LinearLayout).removeView(cardView)
                }
            }
        }

        return cardView
    }

    // Função para carregar pedidos do banco
    private fun carregarPedidos(linearLayoutPedidos: LinearLayout) {
        CoroutineScope(Dispatchers.IO).launch {
            val pedidosSalvos = pedidoDao.obterTodosPedidos()
            withContext(Dispatchers.Main) {
                pedidosSalvos.forEach { pedido ->
                    val pedidoCard = createPedidoCard(pedido)
                    linearLayoutPedidos.addView(pedidoCard)
                }
            }
        }
    }

    // Função para abrir a galeria e escolher uma imagem
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Lidar com a imagem selecionada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            imageUri = data?.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
