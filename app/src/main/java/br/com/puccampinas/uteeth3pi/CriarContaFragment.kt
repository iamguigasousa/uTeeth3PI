package br.com.puccampinas.uteeth3pi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import br.com.puccampinas.uteeth3pi.databinding.FragmentCriarContaBinding
import br.com.puccampinas.uteeth3pi.CustomResponse
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.storage.ktx.storage
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.UUID


/**
 * Fragment para o cadastro de conta.
 */
class CriarContaFragment : Fragment() {

    private val TAG = "SignUpFragment"
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    private var _binding: FragmentCriarContaBinding? = null
    private val binding get() = _binding!!

    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCriarContaBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgbArrow.setOnClickListener {
            findNavController().navigate(R.id.action_CriarContaFragment_to_LoginFragment)
        }

        binding.btnCadastrar.setOnClickListener {
            // criar a conta...
            signUpNewAccount(
                binding.etNome.text.toString(),
                binding.etTelefoneCreate.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etAddress1.text.toString(),
                binding.etAddress2.text.toString(),
                binding.etAddress3.text.toString(),
                binding.etCurriculum.text.toString(),
                (activity as MainActivity).getFcmToken()
            );
        }

        binding.btnFoto.setOnClickListener {
            dispatchTakePictureIntent()
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


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imgViewteste.setImageBitmap(imageBitmap)

            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val data = byteArrayOutputStream.toByteArray()

            // Armazenar a foto no Firebase Storage
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("${auth.currentUser?.uid}/${UUID.randomUUID()}.jpg")

            val uploadTask = imageRef.putBytes(data)
            uploadTask.continueWithTask(Continuation {
                if (!it.isSuccessful) {
                    it.exception?.let { e ->
                        throw e
                    }
                }
                imageRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    // Aqui você pode usar o downloadUri para salvar a URL da imagem no Firestore ou onde mais for necessário.
                    Log.d(TAG, "Imagem enviada com sucesso. URL: $downloadUri")
                    Snackbar.make(requireView(),"Foto Enviada com Sucesso!",Snackbar.LENGTH_LONG).show()
                } else {
                    Log.e(TAG, "Erro ao enviar a imagem", task.exception)
                }
            }
        }
    }




    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun signUpNewAccount(nome: String, telefone: String, email: String, password: String, address1: String, address2: String, address3: String, curriculum: String, fcmToken: String) {
        auth = Firebase.auth
        // auth.useEmulator("127.0.0.1", 5001)
        // invocar a função e receber o retorno fazendo Cast para "CustomResponse"

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    (activity as MainActivity).storeUserId(user!!.uid)
                    // atualizar o perfil do usuário com os dados chamando a function.
                    updateUserProfile(nome, telefone, email, user!!.uid, address1, address2, address3, curriculum, fcmToken)
                        .addOnCompleteListener(requireActivity()) { res ->
                            // conta criada com sucesso.
                            if(res.result.status == "SUCCESS"){
                                hideKeyboard()
                                Snackbar.make(requireView(),"Conta cadastrada!",Snackbar.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_CriarContaFragment_to_LoginFragment)
                            }

                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                    // dar seguimento ao tratamento de erro.
                }
            }
    }

    private fun updateUserProfile(nome: String, telefone: String, email: String, uid: String, address1: String, address2: String, address3: String,curriculum: String, fcmToken: String) : Task<CustomResponse>{
        // chamar a function para atualizar o perfil.
        functions = Firebase.functions("southamerica-east1")

        // Create the arguments to the callable function.
        val data = hashMapOf(
            "name" to nome,
            "phone" to telefone,
            "email" to email,
            "uid" to uid,
            "address1" to address1,
            "address2" to address2,
            "address3" to address3,
            "curriculum" to curriculum,
            "fcmToken" to fcmToken
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