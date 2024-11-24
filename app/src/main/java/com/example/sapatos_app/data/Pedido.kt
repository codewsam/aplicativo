package com.example.sapatos_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos")
data class Pedido(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nomeCliente: String,
    val dataPedido: String,
    val produto: String,
    val imagemUri: String? = null // Armazena a URI da imagem, pode ser nula
)
