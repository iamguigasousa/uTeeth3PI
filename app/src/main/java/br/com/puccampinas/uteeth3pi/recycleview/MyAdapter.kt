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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyAdapter(var context: Context, var userArrayList: ArrayList<User>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = userArrayList[position]


        holder.name.text = user.name
        holder.phone.text = user.phone
        holder.uid.text = user.uid
        holder.fcmToken.text = user.fcmToken

    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val phone: TextView = itemView.findViewById(R.id.tv_phone)
        val uid: TextView = itemView.findViewById(R.id.tv_uid)
        val fcmToken: TextView = itemView.findViewById(R.id.tv_fcm)



        var btn_aceitar: Button

        init {
            btn_aceitar.setOnClickListener{
                val db = Firebase.firestore
                val docRef = db.collection("dentista").document()

                // Atualizar o documento
                docRef.update("nome", "Renata Teste Button")
                    .addOnSuccessListener {
                        // Atualização bem-sucedida
                    }
                    .addOnFailureListener { e ->
                        // Ocorreu um erro ao atualizar o documento
                    }
        }






    }








    }
}



