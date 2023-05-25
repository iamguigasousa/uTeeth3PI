package br.com.puccampinas.uteeth3pi

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import br.com.puccampinas.uteeth3pi.databinding.FragmentAccountDetailsBinding
import br.com.puccampinas.uteeth3pi.databinding.FragmentHomeMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import org.json.JSONObject


class AccountDetailsFragment : Fragment() {
    private var _binding: FragmentAccountDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var nomeUsuario: TextView
    private lateinit var emailUsuario: TextView

    private lateinit var phoneUsuario: EditText
    private lateinit var address1Usuario: EditText
    private lateinit var address2Usuario: EditText
    private lateinit var address3Usuario: EditText
    private lateinit var curriculoUsuario: EditText
    private lateinit var btn_update: Button





    private lateinit var auth: FirebaseAuth
    private var gson = GsonBuilder().enableComplexMapKeySerialization().create()


    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var usuarioID: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)

        IniciarComponentes()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid

        val docRef = db.collection("dentista").document(uid)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    var data = gson.fromJson((JSONObject(document.data).toString()),data::class.java)
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




        binding.btnVoltar.setOnClickListener {
            findNavController().navigate(R.id.action_accountDetailsFragment_to_homeMenuFragment)
        }
    }


    private fun IniciarComponentes() {
        nomeUsuario = binding.textView
        emailUsuario = binding.textView2
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

