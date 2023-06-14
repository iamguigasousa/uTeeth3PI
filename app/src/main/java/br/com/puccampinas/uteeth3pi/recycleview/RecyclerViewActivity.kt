package br.com.puccampinas.uteeth3pi.recycleview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.ChamadosAceitosFragment
import br.com.puccampinas.uteeth3pi.HomeMenuFragment
import br.com.puccampinas.uteeth3pi.MainActivity
import br.com.puccampinas.uteeth3pi.R
import br.com.puccampinas.uteeth3pi.datastore.UserPreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

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
        val collectionRef = firestore.collection("Chamados")
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
