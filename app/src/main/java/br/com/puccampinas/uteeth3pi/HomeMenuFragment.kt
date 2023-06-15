package br.com.puccampinas.uteeth3pi

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.android.gms.tasks.Continuation
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID


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
    private val REQUEST_IMAGE_CAPTURE = 1



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

            val status = userPreferencesRepository.status

            val snackbar = Snackbar.make(requireView(), "Status Atualizado!", Snackbar.LENGTH_LONG)

// Defina as cores para os diferentes estados do status
            val greenColor = resources.getColor(android.R.color.holo_green_light)
            val redColor = resources.getColor(android.R.color.holo_red_light)

// Defina a cor de fundo da Snackbar com base no status
            if (status) {
                snackbar.view.setBackgroundColor(greenColor)
            } else {
                snackbar.view.setBackgroundColor(redColor)
            }

            snackbar.show()
        }
        binding.llDisponivel.setOnClickListener {

            userPreferencesRepository.updateStatus(!userPreferencesRepository.status)

            OnStatus(userPreferencesRepository.status)
            Snackbar.make(requireView(),"Status Atualizado!", Snackbar.LENGTH_LONG).show()

            val status = userPreferencesRepository.status

            val snackbar = Snackbar.make(requireView(), "Status Atualizado!", Snackbar.LENGTH_LONG)

// Defina as cores para os diferentes estados do status
            val greenColor = resources.getColor(android.R.color.holo_green_light)
            val redColor = resources.getColor(android.R.color.holo_red_light)

// Defina a cor de fundo da Snackbar com base no status
            if (status) {
                snackbar.view.setBackgroundColor(greenColor)
            } else {
                snackbar.view.setBackgroundColor(redColor)
            }

            snackbar.show()
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

        binding.imageView.setOnClickListener {
            dispatchTakePictureIntent()
            loadImageFromStorage(uid)
        }

        //ir para detalhes da conta
        binding.llDetalhesconta.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_accountDetailsFragment)
        }
        binding.btnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_accountDetailsFragment)
        }

        //navegar para emergencias
        binding.llEmergencias.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_recyclerViewActivity)
        }
        binding.btnEmergencias.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_recyclerViewActivity)
        }

        //navegar chamados aceitos
        binding.llAceitos.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_ChamadosActivity)
        }
        binding.btnChamados.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_ChamadosActivity)
        }


        binding.btnDeslogar.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_homeMenuFragment_to_LoginFragment)
        }




    }


    private fun loadImageFromStorage(uid: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/$uid.jpg") // Substitua "images" pelo caminho correto para suas imagens

        val localFile = File.createTempFile("tempImage", "jpg") // Crie um arquivo temporário para armazenar a imagem

        imageRef.getFile(localFile)
            .addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                binding.imageView.setImageBitmap(bitmap)
            }
            .addOnFailureListener { exception ->

                Log.e(TAG, "Erro ao baixar a imagem: ${exception.message}", exception)
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
            binding.imageView.setImageBitmap(imageBitmap)

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

