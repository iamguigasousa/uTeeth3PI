package br.com.puccampinas.uteeth3pi.recycleview

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder


private lateinit var btnArrow: ImageButton
private lateinit var recyclerView: RecyclerView
private lateinit var adapter: ChamadosAdapter
private lateinit var auth: FirebaseAuth

class RecyclerViewChamadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_chamados)

        recyclerView = findViewById(R.id.recyclerViewChamados)

        btnArrow = findViewById(R.id.imgb_arrow2)

        recyclerView.layoutManager = LinearLayoutManager(this)

        chamarFirebase()



        val btnArrow = findViewById<ImageButton>(R.id.imgb_arrow2)


//        btnArrow.setOnClickListener {
//
//            val fragment = HomeMenuFragment()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container2, fragment)
//                .commit()
//
//        }

    }
    private fun chamarFirebase(){
        var gson = GsonBuilder().enableComplexMapKeySerialization().create()
        auth = Firebase.auth
        val user = auth.currentUser
        val userUid = user!!.uid


        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("Chamados")
            .whereEqualTo("uidDentista",userUid).whereEqualTo("status","Accept")
        collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Tratar erros
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val items = snapshot.documents
                adapter = ChamadosAdapter(items)
                recyclerView.adapter = adapter
            }
        }
    }
}