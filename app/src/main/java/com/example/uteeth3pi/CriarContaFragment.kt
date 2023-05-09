package com.example.uteeth3pi

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.uteeth3pi.databinding.FragmentCriarContaBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CriarContaFragment : Fragment() {

    private val TAG = "SignUpFragment"
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    private var _binding: FragmentCriarContaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCriarContaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCadastrar.setOnClickListener {
            // criar a conta...
            signUpNewAccount(
                binding.etNome.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etTelefoneCreate.text.toString(),
                binding.etAddress1.text.toString(),
                binding.etAddress2.text.toString(),
                binding.etAddress3.text.toString(),
                (activity as MainActivity).getFcmToken()
            );
        }
    }

    fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }

    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun signUpNewAccount(nome: String, email: String, password: String, phone: String, address1: String, address2: String, address3: String, fcmToken: String) {
        auth = Firebase.auth
        // auth.useEmulator("127.0.0.1", 5001)
        // invocar a função e receber o retorno fazendo Cast para "CustomResponse"

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    Snackbar.make(requireView(),"Conta cadastrada! Pode fazer o login!",
                        Snackbar.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_CriarContaFragment_to_LoginFragment)

                } else{
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                    // dar seguimento ao tratamento de erro.
                }
            }
        collection(nome, email, phone, fcmToken, address1, address2, address3)
    }

    private fun collection(nome: String, email: String, phone: String, fcmToken: String, address1: String, address2: String, address3: String) : Task<CustomResponse> {
        //
        functions = Firebase.functions("southamerica-east1")

        // Create the arguments to the callable function.
        val data = hashMapOf(
            "name" to nome,
            "email" to email,
            "phone" to phone,
            "fcmToken" to fcmToken,
            "address[address1]" to address1,
            "address[address2]" to address2,
            "address[address3]" to address3,
            "curriculum" to "Pro Player de Fortnite, 10 anos de jogatina"
        )

        return functions
            .getHttpsCallable("createDentista")
            .call(data)
            .continueWith { task ->

                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}