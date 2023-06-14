package br.com.puccampinas.uteeth3pi

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.puccampinas.uteeth3pi.databinding.ActivityChamadosBinding
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChamadosActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityChamadosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChamadosBinding.inflate(layoutInflater)
        firestore = Firebase.firestore
        setContentView(binding.root)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        auth = FirebaseAuth.getInstance()

        getDados()

        binding.btnLocalizacao.setOnClickListener {
            sendLocalizacao()


        }





    }

    private fun sendLocalizacao(){
        // Verificar permissões de localização
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permissões de localização
            val REQUEST_LOCATION_PERMISSION = 123
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            // Permissões de localização concedidas, obter a localização atual
            getLocalizacaoAtual()
        }

    }

    private fun getDados(){

        val user = auth.currentUser
        val userUid = user!!.uid


        val collectionRef = firestore.collection("Chamados")
            .whereEqualTo("uidDentista",userUid).whereEqualTo("status","Accept")
        collectionRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    binding.tvName.text = document.getString("name").toString()
                    binding.tvPhone.text = document.getString("phone").toString()

                    // Faça algo com os dados obtidos, como atualizar os TextViews
                }
            }
            .addOnFailureListener { exception ->
                // Trate qualquer erro que ocorra durante a obtenção dos dados
            }


    }

    private fun getLocalizacaoAtual() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Enviar a localização para o Firebase Firestore
                if (location != null) {
                    enviarLocalizacaoParaFirestore(location.latitude, location.longitude)
                }
            }
            .addOnFailureListener { exception: Exception ->
                // Lidar com falha ao obter a localização
                // ...
            }
    }

    private fun enviarLocalizacaoParaFirestore(latitude: Double, longitude: Double) {
        val db = FirebaseFirestore.getInstance()
        val localizacao = hashMapOf(
            "latitude" to latitude.toString(),
            "longitude" to longitude.toString()
        )

        db.collection("Chamados").document("K00aG0V0i0q8TX1mdJPX")
            .collection("localização")
            .add(localizacao)
            .addOnSuccessListener { documentReference ->

                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Localização enviada com sucesso!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Falha ao enviar a localização!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }
}
