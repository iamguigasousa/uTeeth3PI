package br.com.puccampinas.uteeth3pi.recyclerview

import MyAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import br.com.puccampinas.uteeth3pi.NotificacaoTesteView
import br.com.puccampinas.uteeth3pi.R
import br.com.puccampinas.uteeth3pi.databinding.FragmentRecyclerViewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class RecyclerViewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<NotificacaoTesteView>
    private var db = Firebase.firestore
    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root

//        binding.recyclerview
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        userList = arrayListOf()
//
//        db = FirebaseFirestore.getInstance()
//
//        db.collection("Chamados").get()
//            .addOnSuccessListener {
//                if (!it.isEmpty){
//                    for (data in it.documents){
//                        val notificacao: NotificacaoTesteView? = data.toObject(NotificacaoTesteView::class.java)
//                        if (notificacao != null) {
//                            userList.add(notificacao)
//                        }
//                    }
//                    recyclerView.adapter = MyAdapter(userList)
//                }
//            }
//            .addOnFailureListener{
//                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
//            }

    }



}