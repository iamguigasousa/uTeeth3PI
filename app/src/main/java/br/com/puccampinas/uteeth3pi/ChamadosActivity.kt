package br.com.puccampinas.uteeth3pi

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.GsonBuilder
import org.json.JSONObject

class ChamadosActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var btnLocalizacao: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chamados)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)




        btnLocalizacao = findViewById(R.id.btn_localizacao)
        btnLocalizacao.setOnClickListener {
            getCurrentLocation()
        }







    }



    private fun getCurrentLocation(){
        if (checkPermissions()){
            if (isLocationEnabled()){

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
                    val location: Location? = task.result
                    if (location == null){
                        Toast.makeText(this, "Nenhum dado recebido", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Localização Enviada", Toast.LENGTH_SHORT).show()
                        tvLatitude.text = location.latitude.toString()
                        tvLongitude.text = location.longitude.toString()

                        val latitude = location.latitude.toString()
                        val longitude = location.longitude.toString()

                        enviarLocalizacaoParaFirestore(latitude, longitude)
                    }

                }

            }
            else{
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            requestPermission()

        }
    }


    private fun enviarLocalizacaoParaFirestore(latitude: String, longitude: String) {
        val db = FirebaseFirestore.getInstance()

        val localizacao = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )

        db.collection("Chamados").document("K00aG0V0i0q8TX1mdJPX")
            .collection("localização")
            .add(localizacao)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Localização enviada para o Firestore", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao enviar para o Firestore", Toast.LENGTH_SHORT).show()

            }
    }

    private fun getDados(){
        var gson = GsonBuilder().enableComplexMapKeySerialization().create()
        auth = Firebase.auth
        val user = auth.currentUser
        val userUid = user!!.uid

        val userRef = db.collection("dentista").document(userUid)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    FirebaseMessaging.getInstance().token.addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w(
                                    ContentValues.TAG,
                                    "Fetching FCM registration token failed",
                                    task.exception
                                )
                                return@OnCompleteListener
                            }

                            // Get new FCM registration token
                            val token = task.result


                            var data = gson.fromJson(
                                (JSONObject(document.data).toString()),
                                data::class.java
                            )

                            val documentRef =
                                db.collection("Chamados").document(uid.toString())
                                    .collection("dentista").document(userUid)

                            val userData = hashMapOf(
                                "name" to data.name,
                                "uid" to data.uid,
                                "fcmToken" to token,
                                "curriculum" to data.curriculum,
                                "phone" to data.phone


                            )

                            documentRef.set(userData)
                                .addOnSuccessListener {

                                }
                                .addOnFailureListener { e ->
                                    // Ocorreu um erro ao adicionar o documento à subcoleção
                                }
                        })

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }


    private fun isLocationEnabled():Boolean{
        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
    }

    private fun checkPermissions(): Boolean{
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
            ==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode== PERMISSION_REQUEST_ACCESS_LOCATION){
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }
            else{
                Toast.makeText(applicationContext,"Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}