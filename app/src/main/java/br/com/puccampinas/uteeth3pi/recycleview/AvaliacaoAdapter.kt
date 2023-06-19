package br.com.puccampinas.uteeth3pi.recycleview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class AvaliacaoAdapter(private val items: List<com.google.firebase.firestore.DocumentSnapshot>) : RecyclerView.Adapter<AvaliacaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvaliacaoViewHolder {
        val itemView = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item3, parent, false)
        return AvaliacaoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AvaliacaoViewHolder, position: Int) {
        val item = items[position]

        val name = item.getString("name")
        val uid = item.getString("uid")

        holder.tv_Nome.text = name

        chamarFirebase(item) { comentario, rate, data ->
            holder.tv_Avaliacao.text = comentario ?: ""
            holder.tv_Estrela.text = rate?.toString() ?: ""
            holder.tv_Data.text = data?.toString() ?: "Data indisponível"
        }
    }

    private fun chamarFirebase(item: com.google.firebase.firestore.DocumentSnapshot, callback: (String?, Any?, Date?) -> Unit) {
        val rateCollectionRef = item.reference.collection("rate")
        rateCollectionRef.addSnapshotListener { rateSnapshot, rateException ->
            if (rateException != null) {
                // Tratar erros
                return@addSnapshotListener
            }

            if (rateSnapshot != null) {
                val documentosRate = rateSnapshot.documents

                for (documentoRate in documentosRate) {
                    // Acessar os dados do documento da subcoleção "rate"
                    val comentario = documentoRate.getString("comentário")
                    val rate = documentoRate.get("nota")
                    val dataTimestamp = documentoRate.getTimestamp("data")
                    val data: Date? = dataTimestamp?.toDate()

                    // Chamar o callback com os valores obtidos
                    callback(comentario, rate, data)
                }
            }
        }
    }
}
