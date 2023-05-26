package br.com.puccampinas.uteeth3pi

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import br.com.puccampinas.uteeth3pi.databinding.FragmentHomeBinding
import br.com.puccampinas.uteeth3pi.datastore.UserPreferencesRepository
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth;
    private lateinit var functions: FirebaseFunctions
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private var _binding:FragmentHomeBinding ? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferencesRepository = UserPreferencesRepository.getInstance(MainActivity())

        binding.btnOnOff.setOnClickListener {
            userPreferencesRepository.updateStatus(!userPreferencesRepository.status)

            OnStatus(userPreferencesRepository.status)
        }

        binding.btnMensagem.setOnClickListener{
            findNavController().navigate(R.id.action_HomeFragment_to_InfoFragment)
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


    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}