package br.com.puccampinas.uteeth3pi.recycleview

import android.view.ViewGroup
import br.com.puccampinas.uteeth3pi.data
import com.google.firebase.firestore.ktx.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AvaliacaoAdapter (private val items: List<com.google.firebase.firestore.DocumentSnapshot>) : androidx.recyclerview.widget.RecyclerView.Adapter<AvaliacaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvaliacaoViewHolder {
        val itemView = android.view.LayoutInflater.from(parent.context).inflate(br.com.puccampinas.uteeth3pi.R.layout.item3, parent, false)
        return AvaliacaoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AvaliacaoViewHolder, position: Int) {
        val db = com.google.firebase.ktx.Firebase.firestore
        val item = items[position]


        val comentario = item.getString("comentário")
        val dataTimestamp = item.getTimestamp("data")
        val estrela = item.getString("nota")

        val name = item.getString("name")

        val uid = item.getString("uid")

        lateinit var auth: com.google.firebase.auth.FirebaseAuth


        holder.tv_Avaliacao.text = comentario.toString()



        if (dataTimestamp != null) {
            val data: Date = dataTimestamp.toDate()
            val dataFormatada = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(data)
            holder.tv_Data.text = dataFormatada
        } else {
            holder.tv_Data.text = "Data indisponível"
        }

        holder.tv_Estrela.text = estrela.toString()

        holder.tv_Nome.text = name















    }
}