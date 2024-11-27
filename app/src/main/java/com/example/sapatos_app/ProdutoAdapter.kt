import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sapatos_app.Produto
import com.example.sapatos_app.R

class ProdutoAdapter(
    private val produtos: List<Produto> // Lista de produtos a exibir
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    // ViewHolder: Vincula as views de cada item do RecyclerView
    class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView: TextView = view.findViewById(R.id.textView)
    }

    // Inflar o layout do item e criar o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    // Vincular dados do produto às views
    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = produtos[position]
        holder.imageView.setImageResource(produto.imageResId)
        holder.textView.text = produto.nome

        // Ação ao clicar na imagem
        holder.imageView.setOnClickListener {
            val context = holder.itemView.context

            // Criar o Dialog
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_imagem_ampliada) // Usar o layout criado

            // Configurar a imagem no Dialog
            val dialogImageView: ImageView = dialog.findViewById(R.id.dialogImageView)
            dialogImageView.setImageResource(produto.imageResId)

            // Exibir o Dialog
            dialog.show()
        }
    }

    // Retorna o tamanho da lista
    override fun getItemCount(): Int = produtos.size
}
