package br.com.puccampinas.uteeth3pi.recycleview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.HomeMenuFragment
import br.com.puccampinas.uteeth3pi.R
import com.google.firebase.firestore.FirebaseFirestore


private lateinit var btnArrow: ImageButton
private lateinit var recyclerView: RecyclerView

class RecyclerViewChamadosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_chamados)

        recyclerView = findViewById(R.id.recyclerViewChamados)

        btnArrow = findViewById(R.id.imgb_arrow2)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("Chamados")




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
}