package br.com.puccampinas.uteeth3pi.recycleview

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.MapActivity
import br.com.puccampinas.uteeth3pi.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChamadosAdapter(private val items: List<DocumentSnapshot>) :
    RecyclerView.Adapter<ChamadosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChamadosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item2, parent, false)
        return ChamadosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChamadosViewHolder, position: Int) {
        val db = Firebase.firestore
        val item = items[position]
        val name = item.getString("name")
        val phone = item.getString("phone")
        val uid = item.getString("uid")
        val latitude = holder.latitude
        val longitude = holder.longitude


        holder.tv_name.text = name;
        holder.tv_phone.text = phone


        holder.btn_localizacao.setOnClickListener {


            val intent = Intent(holder.itemView.context, MapActivity::class.java)
            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun enviarLocalizacaoParaFirestore(latitude: Double, longitude: Double) {
        val db = FirebaseFirestore.getInstance()

        val localizacao = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )

        db.collection("localizacoes")
            .add(localizacao)
            .addOnSuccessListener { documentReference ->
                // Sucesso ao enviar a localização
            }
            .addOnFailureListener { e ->
                // Erro ao enviar a localização
            }
    }


    override fun getItemCount(): Int {
        return items.size
    }


}
