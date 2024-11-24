package com.example.sapatos_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirPedido(pedido: Pedido)

    @Query("SELECT * FROM pedidos")
    suspend fun obterTodosPedidos(): List<Pedido>

    @Query("DELETE FROM pedidos WHERE id = :id")
    suspend fun deletarPedido(id: Int)
}
