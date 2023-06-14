package br.com.puccampinas.uteeth3pi.recycleview

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.R
import com.google.firebase.firestore.FirebaseFirestore

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    private lateinit var btnArrow: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        recyclerView = findViewById(R.id.recyclerView)

        btnArrow = findViewById(R.id.imgb_arrow)

        recyclerView.layoutManager = LinearLayoutManager(this)

        chamarFirebase()




        val btnArrow = findViewById<ImageButton>(R.id.imgb_arrow)



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
