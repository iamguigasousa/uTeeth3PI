package br.com.puccampinas.uteeth3pi.recycleview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.MainActivity
import br.com.puccampinas.uteeth3pi.R
import br.com.puccampinas.uteeth3pi.recycleview.MyAdapter.MyViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyAdapter(val context: Context, val userArrayList: ArrayList<User>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = userArrayList[position]


        holder.name.text = user.name
        holder.phone.text = user.phone
        holder.uidSocorrista.text = user.uidSocorrista
        holder.fcmToken.text = user.fcmToken

    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        val name: TextView = itemView.findViewById(R.id.tv_name)
        val phone: TextView = itemView.findViewById(R.id.tv_phone)
        val uidSocorrista: TextView = itemView.findViewById(R.id.tv_uid)
        val fcmToken: TextView = itemView.findViewById(R.id.tv_fcm)
        var btn_aceitar: TextView = itemView.findViewById(R.id.btn_aceitar)


        init {


            val db = Firebase.firestore









            btn_aceitar.setOnClickListener { view ->
                btn_aceitar = uidSocorrista
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                val documentRef = db.collection("Chamados").document()

                val subcollectionRef = documentRef.collection("dentista")

                val intent = Intent(view.context, MainActivity::class.java)
                view.context.startActivity(intent)

                val novoDocumentoRef = subcollectionRef.document(userId)
                val novoDocumentoDados = hashMapOf(
                    "name" to "valor1",
                    "uid" to "valor2",
                    "fcmToken" to "valor2",
                    "curriculum" to "valor2"

                )
                novoDocumentoRef.set(novoDocumentoDados)
                    .addOnSuccessListener {
                        // Documento adicionado com sucesso à subcoleção
                    }
                    .addOnFailureListener { e ->
                        // Ocorreu um erro ao adicionar o documento à subcoleção
                    }



            }
        }
    }
}




























