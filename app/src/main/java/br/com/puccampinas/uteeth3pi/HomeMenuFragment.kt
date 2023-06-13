package br.com.puccampinas.uteeth3pi

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import br.com.puccampinas.uteeth3pi.databinding.FragmentHomeMenuBinding
import br.com.puccampinas.uteeth3pi.datastore.UserPreferencesRepository
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject


class HomeMenuFragment : Fragment() {
    private var _binding: FragmentHomeMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var nomeUsuario: TextView
    private lateinit var emailUsuario: TextView
    private lateinit var bt_deslogar: Button
    private lateinit var auth: FirebaseAuth
    private var gson = GsonBuilder().enableComplexMapKeySerialization().create()

    private lateinit var functions: FirebaseFunctions
    private lateinit var userPreferencesRepository: UserPreferencesRepository



    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var usuarioID: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeMenuBinding.inflate(inflater, container, false)

        IniciarComponentes()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid

        val docRef = db.collection("dentista").document(uid)

        userPreferencesRepository = UserPreferencesRepository.getInstance(MainActivity())

        binding.btnLiga.setOnClickListener {
            userPreferencesRepository.updateStatus(!userPreferencesRepository.status)

            OnStatus(userPreferencesRepository.status)
            Snackbar.make(requireView(),"Status Atualizado!", Snackbar.LENGTH_LONG).show()
        }


        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    var data = gson.fromJson((JSONObject(document.data).toString()),data::class.java)
                    binding.textView.text=data.name
                    binding.textView2.text=data.email
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


        binding.btnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_accountDetailsFragment)
        }

        binding.btnEmergencias.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_recyclerViewActivity)
        }

        binding.btnChamados.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_recyclerViewChamadosActivity)
        }


        binding.btnDeslogar.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_homeMenuFragment_to_LoginFragment)
        }




    }


    private fun OnStatus(status: Boolean) : Task<CustomResponse> {

        // chamar a function para atualizar o perfil.
        functions = Firebase.functions("southamerica-east1")

        // Create the arguments to the callable function.
        val data = hashMapOf(
            "uid" to (activity as MainActivity).getUserUid(),
            "status" to status
        )


        return functions
            .getHttpsCallable("changeStatus")
            .call(data)
            .continueWith { task ->
                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }

    private fun IniciarComponentes() {
        nomeUsuario = binding.textView
        emailUsuario = binding.textView2
        bt_deslogar = binding.btnDeslogar
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

