package br.com.puccampinas.uteeth3pi.recycleview

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.R
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    var userArrayList: ArrayList<User>? = null
    var myAdapter: MyAdapter? = null
    var db: FirebaseFirestore? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Atualizando Data...")
        progressDialog!!.show()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        db = FirebaseFirestore.getInstance()
        userArrayList = ArrayList()
        myAdapter = MyAdapter(this@RecyclerViewActivity, userArrayList!!)
        recyclerView.setAdapter(myAdapter)
        EventChangeListener()
    }

    private fun EventChangeListener() {
        db!!.collection("Chamados").orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(EventListener { value, error ->
                    if (error != null) {
                        if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                        Log.e("Firestore error", error.message!!)
                        return@EventListener
                    }
                    for (dc in value!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userArrayList!!.add(dc.document.toObject(User::class.java))
                        }
                        myAdapter!!.notifyDataSetChanged()
                        if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                    }
                })
    }
}