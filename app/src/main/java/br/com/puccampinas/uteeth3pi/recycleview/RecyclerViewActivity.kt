package br.com.puccampinas.uteeth3pi.recycleview

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.HomeMenuFragment
import br.com.puccampinas.uteeth3pi.MainActivity
import br.com.puccampinas.uteeth3pi.R
import com.google.firebase.firestore.FirebaseFirestore

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView = findViewById(R.id.recyclerView)



        recyclerView.layoutManager = LinearLayoutManager(this)

        chamarFirebase()




    }
    private fun chamarFirebase(){
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("Chamados").whereEqualTo("status","open")
        collectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Tratar erros
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val items = snapshot.documents
                adapter = MyAdapter(items)
                recyclerView.adapter = adapter
            }
        }
    }






}
