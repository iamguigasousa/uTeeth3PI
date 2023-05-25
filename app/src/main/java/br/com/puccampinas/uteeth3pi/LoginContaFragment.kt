package br.com.puccampinas.uteeth3pi

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import br.com.puccampinas.uteeth3pi.databinding.FragmentLoginContaBinding
import br.com.puccampinas.uteeth3pi.datastore.UserPreferencesRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginContaFragment : Fragment() {

    private lateinit var email: String;
    private lateinit var password: String;
    private lateinit var auth: FirebaseAuth;

    private var _binding: FragmentLoginContaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginContaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyLogin()

        // evento para tratar o login com auth.
        binding.btnSignIn.setOnClickListener {
            // antes de tentar o login, implemente as validações.
            login(binding.etEmail.text.toString(), binding.etPassword.text.toString());
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_CriarContaFragment)
        }
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun newLogin() {
        binding.btnSignIn.hideKeyboard()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
            login(email!!, password!!)
        }
    }

    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun login(email: String, password: String){
        hideKeyboard()
        // inicializando o auth.
        auth = Firebase.auth

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener (OnCompleteListener  { task ->
                if (task.isSuccessful) {
                    // login completado com sucesso.

                    (activity as MainActivity).storeUserId(task.result.user!!.uid)

                    findNavController().navigate(R.id.action_LoginFragment_to_homeMenuFragment)
                } else {
                    if (task.exception is FirebaseAuthException) {
                        Snackbar.make(requireView(),"Não foi possível fazer o login, verifique os dados e tente novamente.", Snackbar.LENGTH_LONG).show()
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun verifyLogin(){
        auth=Firebase.auth

        if(auth.currentUser!=null) {
            findNavController().navigate(R.id.action_LoginFragment_to_homeMenuFragment)
        }
    }
}