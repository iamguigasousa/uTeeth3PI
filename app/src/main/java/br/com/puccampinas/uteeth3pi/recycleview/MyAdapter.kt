package br.com.puccampinas.uteeth3pi.recycleview

import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.MainActivity
import br.com.puccampinas.uteeth3pi.R
import br.com.puccampinas.uteeth3pi.data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import org.json.JSONObject

class MyAdapter(private val items: List<DocumentSnapshot>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = Firebase.firestore
        val item = items[position]
        val name = item.getString("name")
        val phone = item.getString("phone")

        holder.tv_name.text = name
        holder.tv_phone.text = phone

        //ação do botão aceitar

        holder.btn_aceitar.setOnClickListener {view ->

            var gson = GsonBuilder().enableComplexMapKeySerialization().create()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val documentRef = db.collection("Chamados").document("K00aG0V0i0q8TX1mdJPX").collection("dentista").document(userId)
            var data = gson.fromJson((JSONObject(document.data).toString()), data::class.java)
            val novoDocumentoDados = hashMapOf(
                "name" to "valor1",
                "uid" to "valor2",
                "fcmToken" to "valor2",
                "curriculum" to "valor2"


            )

            documentRef.set(novoDocumentoDados)
                .addOnSuccessListener {

                }
                .addOnFailureListener { e ->
                    // Ocorreu um erro ao adicionar o documento à subcoleção
                }
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {


                        binding.textView.text=data.name
                        binding.textView2.text=data.email
                        binding.tvNomeProfile2.setText(data.name)
                        binding.tvEmailProfile2.setText(data.email)
                        binding.tvPhoneProfile2.setText(data.phone)
                        binding.tvEndereco1Profile2.setText(data.address1)
                        binding.tvEndereco2Profile2.setText(data.address2)
                        binding.tvEndereco3Profile2.setText(data.address3)
                        binding.tvCurriculoProfile2.setText(data.curriculum)
                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }

            val intent = Intent(view.context, MainActivity::class.java)
            view.context.startActivity(intent)




        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
