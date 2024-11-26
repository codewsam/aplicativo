package com.example.sapatos_app

import PedidoDatabaseHelper
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
import java.io.IOException

class PedidosActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: android.net.Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        val btnAddPedido: Button = findViewById(R.id.btnAdicionarPedido)
        val linearLayoutPedidos = findViewById<LinearLayout>(R.id.linearLayoutPedidos)
        val btnVoltar: Button = findViewById(R.id.btnVoltar)

        // Instanciando o banco de dados e buscando os pedidos
        val pedidoDatabaseHelper = PedidoDatabaseHelper(this)
        val pedidos = pedidoDatabaseHelper.getAllPedidos()

        // Exibindo os pedidos no LinearLayout
        for (pedido in pedidos) {
            val pedidoCard = createPedidoCard(pedido.nomeCliente, pedido.dataPedido, pedido.produto)
            if (pedido.imagem != null) {
                val imageView: ImageView = pedidoCard.findViewById(R.id.imagePedido)
                imageView.setImageURI(android.net.Uri.parse(pedido.imagem))
            }
            linearLayoutPedidos.addView(pedidoCard)
        }

        btnAddPedido.setOnClickListener {
            showAddPedidoDialog(linearLayoutPedidos)
        }

        btnVoltar.setOnClickListener {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        val pedidoDatabaseHelper = PedidoDatabaseHelper(this)
        val pedidos = pedidoDatabaseHelper.getAllPedidos()

        val linearLayoutPedidos = findViewById<LinearLayout>(R.id.linearLayoutPedidos)
        linearLayoutPedidos.removeAllViews()  // Limpa a tela antes de adicionar os pedidos

        // Exibe os pedidos no layout
        for (pedido in pedidos) {
            val pedidoCard = createPedidoCard(pedido.nomeCliente, pedido.dataPedido, pedido.produto)
            if (pedido.imagem != null) {
                val imageView: ImageView = pedidoCard.findViewById(R.id.imagePedido)
                imageView.setImageURI(android.net.Uri.parse(pedido.imagem))
            }
            linearLayoutPedidos.addView(pedidoCard)
        }
    }


    // Função para exibir o AlertDialog para adicionar um pedido
    @SuppressLint("MissingInflatedId")
    // Dentro da PedidosActivity
    private fun showAddPedidoDialog(linearLayoutPedidos: LinearLayout) {

        val dialogView = layoutInflater.inflate(R.layout.dialog_add_pedido, null)
        val nomeClienteEditText = dialogView.findViewById<EditText>(R.id.editNomeCliente)
        val dataPedidoDatePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
        val produtoEditText = dialogView.findViewById<EditText>(R.id.editProduto)

        // Botão para selecionar imagem
        val btnSelectImage: Button = dialogView.findViewById(R.id.btnSelectImage)
        btnSelectImage.setOnClickListener {
            openGallery()
        }

        // Criando o AlertDialog para adicionar um pedido
        val dialog = AlertDialog.Builder(this)
            .setTitle("Adicionar Pedido")
            .setView(dialogView)
            .setPositiveButton("Adicionar") { _, _ ->
                

                val nomeCliente = nomeClienteEditText.text.toString()
                val dataPedido = "${dataPedidoDatePicker.dayOfMonth}/${dataPedidoDatePicker.month + 1}/${dataPedidoDatePicker.year}"
                val produto = produtoEditText.text.toString()

                if (nomeCliente.isNotEmpty() && produto.isNotEmpty()) {
                    // Salvar no banco de dados
                    val pedidoDatabaseHelper = PedidoDatabaseHelper(this)
                    val imagemUriString = imageUri?.toString() // Converte a URI da imagem em String
                    pedidoDatabaseHelper.addPedido(nomeCliente, dataPedido, produto, imagemUriString)

                    // Criar o CardView para exibir o pedido
                    val pedidoCard = createPedidoCard(nomeCliente, dataPedido, produto)
                    // Adicionar a imagem se houver
                    if (imageUri != null) {
                        val imageView: ImageView = pedidoCard.findViewById(R.id.imagePedido)
                        imageView.setImageURI(imageUri)
                    }
                    // Adicionar o CardView ao LinearLayout
                    linearLayoutPedidos.addView(pedidoCard)
                } else {
                    Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    // Função para criar o CardView com as informações do pedido
    private fun createPedidoCard(nomeCliente: String, dataPedido: String, produto: String): CardView {
        // Inflar o layout do CardView
        val cardView = LayoutInflater.from(this).inflate(R.layout.item_pedido, null) as CardView

        // Atribuir os valores aos TextViews dentro do CardView
        val textNomeCliente = cardView.findViewById<TextView>(R.id.textNomeCliente)
        val textDataPedido = cardView.findViewById<TextView>(R.id.textDataPedido)
        val textProduto = cardView.findViewById<TextView>(R.id.textProduto)

        textNomeCliente.text = "Cliente: $nomeCliente"
        textDataPedido.text = "Data: $dataPedido"
        textProduto.text = "Produto: $produto"

        return cardView
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
                // Aqui você pode fazer algo com a imagem se necessário, como exibir
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}