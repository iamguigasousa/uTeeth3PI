package br.com.puccampinas.uteeth3pi.recycleview

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.ChamadosAceitosFragment
import br.com.puccampinas.uteeth3pi.MainActivity
import br.com.puccampinas.uteeth3pi.R
import br.com.puccampinas.uteeth3pi.data
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
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
        val uid = item.getString("uid")
        val photo = item.getString("photoPath")

        lateinit var auth: FirebaseAuth

        holder.tv_name.text = name
        holder.tv_phone.text = phone
        Glide.with(holder.itemView).load(photo).into(holder.tv_photo)

        //ação do botão aceitar

        holder.btn_aceitar.setOnClickListener { view ->

            var gson = GsonBuilder().enableComplexMapKeySerialization().create()
            auth = Firebase.auth
            val user = auth.currentUser
            val userUid = user!!.uid

            val userRef = db.collection("dentista").document(userUid)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(
                                        TAG,
                                        "Fetching FCM registration token failed",
                                        task.exception
                                    )
                                    return@OnCompleteListener
                                }

                                // Get new FCM registration token
                                val token = task.result


                                var data = gson.fromJson(
                                    (JSONObject(document.data).toString()),
                                    data::class.java
                                )

                                val documentRef =
                                    db.collection("Chamados").document(uid.toString())
                                        .collection("dentista").document(userUid)

                                val userData = hashMapOf(
                                    "name" to data.name,
                                    "uid" to data.uid,
                                    "fcmToken" to token,
                                    "curriculum" to data.curriculum,
                                    "phone" to data.phone


                                )

                                documentRef.set(userData)
                                    .addOnSuccessListener {

                                    }
                                    .addOnFailureListener { e ->
                                        // Ocorreu um erro ao adicionar o documento à subcoleção
                                    }

                                val intent = Intent(view.context, MainActivity::class.java)

                                view.context.startActivity(intent)
                            })

                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
