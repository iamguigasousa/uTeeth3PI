package br.com.puccampinas.uteeth3pi.recycleview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.util.Date


private lateinit var recyclerView: RecyclerView
private lateinit var adapter: AvaliacaoAdapter
private lateinit var auth: FirebaseAuth


class AvaliacaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_recycler_view_avaliacao)
        recyclerView = findViewById(R.id.recyclerViewAvaliacao)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        chamarFirebase()
    }



//    private fun chamarFirebase(){
//        val firestore = FirebaseFirestore.getInstance()
//        val collectionRef = firestore.collection("Chamados").whereNotEqualTo("uidDentista", null)
//
//        collectionRef.addSnapshotListener { snapshot, exception ->
//            if (exception != null) {
//                // Tratar erros
//                return@addSnapshotListener
//            }
//
//            if (snapshot != null) {
//                val items = snapshot.documents
//                adapter = AvaliacaoAdapter(items)
//                recyclerView.adapter = adapter
//            }
//        }
//    }



    private fun chamarFirebase() {
        val firestore = FirebaseFirestore.getInstance()
        val chamadosCollectionRef = firestore.collection("Chamados")

        chamadosCollectionRef.whereNotEqualTo("uidDentista", null)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Tratar erros
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val items = snapshot.documents


                    val documentosChamados = snapshot.documents

                    for (documentoChamado in documentosChamados) {
                        val rateCollectionRef = documentoChamado.reference.collection("rate")

                        rateCollectionRef.addSnapshotListener { rateSnapshot, rateException ->
                            if (rateException != null) {
                                // Tratar erros
                                return@addSnapshotListener
                            }

                            if (rateSnapshot != null) {
                                val documentosRate = rateSnapshot.documents



                                for (documentoRate in documentosRate) {
                                    // Acessar os dados do documento da subcoleção "rate"
                                    val comentario_av = documentoRate.getString("comentário")
                                    val dataTimestamp = documentoRate.getTimestamp("data")
                                    val rate_av = documentoRate.get("nota")

                                    val data: Date? = dataTimestamp?.toDate()

                                    // Faça o que você precisa com os dados do documento
                                    println("rate: $rate_av, data: $data, comentário: $comentario_av")

                                }
                                adapter = AvaliacaoAdapter(items)
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                }
            }
    }




}