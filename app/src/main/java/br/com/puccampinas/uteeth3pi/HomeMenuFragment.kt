package br.com.puccampinas.uteeth3pi


import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import br.com.puccampinas.uteeth3pi.databinding.FragmentHomeMenuBinding
import br.com.puccampinas.uteeth3pi.datastore.UserPreferencesRepository
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import org.json.JSONObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FileDownloadTask
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

        val imageView = binding.imageView
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
            exibirImagemUsuario(uid, imageView)

        }

        binding.btnAvaliacao.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_avaliacaoActivity)
        }

        binding.btnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_accountDetailsFragment)
        }

        binding.btnEmergencias.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_recyclerViewActivity)
        }

        binding.btnChamados.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_ChamadosActivity)
        }


        binding.btnDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_homeMenuFragment_to_LoginFragment)
        }




    }



    // Função para exibir a imagem do usuário no ImageView
    fun exibirImagemUsuario(uid: String, imageView: ImageView) {
        // Referência ao Storage do Firebase
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference

        // Caminho da imagem no Storage (substitua "imagens_usuarios" pelo caminho correto)
        val imagePath = ("${auth.currentUser?.uid}/.jpg")

        // Cria um arquivo temporário local para salvar a imagem baixada
        val localFile = File.createTempFile("tempImage", "jpg")

        // Baixa a imagem do Storage para o arquivo local temporário
        storageRef.child(imagePath).getFile(localFile)
            .addOnSuccessListener {
                // Imagem baixada com sucesso, carrega no ImageView
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                imageView.setImageBitmap(bitmap)
            }
            .addOnFailureListener {
                // Ocorreu um erro ao baixar a imagem
                // Lida com o erro de acordo com seus requisitos
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

