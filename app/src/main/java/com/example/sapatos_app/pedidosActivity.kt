package com.example.sapatos_app

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class PedidosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos)

        // Referência para o botão "+"
        val btnAddPedido: Button = findViewById(R.id.btnAdicionarPedido)

        // Definindo a ação do botão "+"
        btnAddPedido.setOnClickListener {
            showAddPedidoDialog()
        }
    }

    // Função para exibir o AlertDialog para adicionar um pedido
    private fun showAddPedidoDialog() {
        // Inicializando os campos de entrada
        val nomeClienteEditText = EditText(this)
        val dataPedidoEditText = EditText(this)
        val produtoEditText = EditText(this)

        // Configurando a caixa de texto para a data
        dataPedidoEditText.setFocusable(false)
        dataPedidoEditText.setClickable(true)
        dataPedidoEditText.setHint("Escolha a data")

        // Criando um DatePickerDialog para escolher a data
        dataPedidoEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    // Formatação da data escolhida
                    val date = "$dayOfMonth/${monthOfYear + 1}/$year"
                    dataPedidoEditText.setText(date)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Construindo o layout do AlertDialog com os campos
        val dialogView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(nomeClienteEditText)
            addView(dataPedidoEditText)
            addView(produtoEditText)
        }

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Adicionar Pedido")
            .setView(dialogView)
            .setPositiveButton("Adicionar") { _, _ ->
                val nomeCliente = nomeClienteEditText.text.toString()
                val dataPedido = dataPedidoEditText.text.toString()
                val produto = produtoEditText.text.toString()

                // Verificar se os campos estão preenchidos
                if (nomeCliente.isNotEmpty() && dataPedido.isNotEmpty() && produto.isNotEmpty()) {
                    // Ação a ser tomada após o envio do pedido (como salvar ou exibir no UI)
                    Toast.makeText(this, "Pedido Adicionado!", Toast.LENGTH_SHORT).show()
                    // Aqui você pode salvar os dados ou atualizar a interface de pedidos
                } else {
                    Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        alertDialog.show()
    }
}
