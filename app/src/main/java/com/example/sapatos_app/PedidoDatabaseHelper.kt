import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PedidoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "pedidos.db"
        const val DATABASE_VERSION = 1
        const val TABLE_PEDIDOS = "pedidos"
        const val COLUMN_ID = "id"
        const val COLUMN_NOME_CLIENTE = "nome_cliente"
        const val COLUMN_DATA_PEDIDO = "data_pedido"
        const val COLUMN_PRODUTO = "produto"
        const val COLUMN_IMAGEM = "imagem"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Criação da tabela
        val CREATE_TABLE = ("CREATE TABLE $TABLE_PEDIDOS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NOME_CLIENTE TEXT," +
                "$COLUMN_DATA_PEDIDO TEXT," +
                "$COLUMN_PRODUTO TEXT," +
                "$COLUMN_IMAGEM TEXT)")
        db?.execSQL(CREATE_TABLE) // Executa a criação da tabela
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Atualiza a tabela se a versão do banco for alterada
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PEDIDOS")
        onCreate(db) // Cria a tabela novamente
    }

    // Método para adicionar um pedido ao banco de dados
    fun addPedido(nomeCliente: String, dataPedido: String, produto: String, imagem: String?): Long {
        val db = writableDatabase // Obtemos o banco de dados em modo de escrita
        val values = ContentValues().apply {
            put(COLUMN_NOME_CLIENTE, nomeCliente)
            put(COLUMN_DATA_PEDIDO, dataPedido)
            put(COLUMN_PRODUTO, produto)
            put(COLUMN_IMAGEM, imagem) // A imagem é armazenada como String (URI)
        }
        val id = db.insert(TABLE_PEDIDOS, null, values) // Insere o pedido no banco e retorna o id
        db.close() // Fechamos o banco após a operação
        return id // Retorna o ID do pedido inserido
    }

    // Método para obter todos os pedidos do banco de dados
    @SuppressLint("Range")
    fun getAllPedidos(): List<Pedido> {
        val pedidos = mutableListOf<Pedido>()
        val db = readableDatabase // Obtemos o banco de dados em modo de leitura
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PEDIDOS", null) // Busca todos os pedidos

        // Itera sobre os resultados
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val nomeCliente = cursor.getString(cursor.getColumnIndex(COLUMN_NOME_CLIENTE))
                val dataPedido = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_PEDIDO))
                val produto = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUTO))
                val imagem = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGEM))

                val pedido = Pedido(id, nomeCliente, dataPedido, produto, imagem)
                pedidos.add(pedido)
            } while (cursor.moveToNext())
        }
        cursor.close() // Fecha o cursor após a consulta
        db.close() // Fecha o banco de dados
        return pedidos // Retorna a lista de pedidos
    }

    fun deletePedido(pedidoId: Long): Int {
        val db = this.writableDatabase
        val args = arrayOf(pedidoId.toString()) // Convertendo para String, porque o método delete espera uma String no lugar de Long
        return db.delete("pedidos", "id = ?", args)
    }

    fun getLastPedidoId(): Long {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM pedidos ORDER BY id DESC LIMIT 1", null)
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            cursor.close()
            return id
        }
        cursor?.close()
        return -1  // Retorna -1 se não encontrar nenhum pedido
    }


}





// Classe que representa um Pedido
data class Pedido(
    val id: Long,
    val nomeCliente: String,
    val dataPedido: String,
    val produto: String,
    val imagem: String? // A imagem é armazenada como uma URI em forma de String
)