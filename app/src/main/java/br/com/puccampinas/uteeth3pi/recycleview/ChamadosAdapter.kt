package br.com.puccampinas.uteeth3pi.recycleview

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChamadosAdapter(private val items: List<DocumentSnapshot>, private val fusedLocationClient: FusedLocationProviderClient) : RecyclerView.Adapter<ChamadosViewHolder>() {


    private var currentPosition: Int = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChamadosViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item2, parent, false)
        return ChamadosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChamadosViewHolder, position: Int) {
        val db = Firebase.firestore
        val item = items[position]
        val name = item.getString("name")
        val phone = item.getString("phone")
        val uid = item.getString("uid")


        holder.btn_localizacao.setOnClickListener {
            currentPosition = position
            obterLocalizacaoAtual { latitude, longitude ->
                if (currentPosition == position) {
                    // Chamar a função para enviar a localização para o Firebase Firestore
                    enviarLocalizacaoFirebase(latitude, longitude)
                }
            }
        }
    }

        fun enviarLocalizacaoParaFirestore(latitude: Double, longitude: Double) {
            val db = FirebaseFirestore.getInstance()

            val localizacao = hashMapOf(
                "latitude" to latitude,
                "longitude" to longitude
            )

            db.collection("localizacoes")
                .add(localizacao)
                .addOnSuccessListener { documentReference ->
                    // Sucesso ao enviar a localização
                }
                .addOnFailureListener { e ->
                    // Erro ao enviar a localização
                }
        }




    override fun getItemCount(): Int {
        return items.size
    }
    private fun obterLocalizacaoAtual(callback: (String, String) -> Unit) {
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
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    callback(latitude, longitude)
                }
            }
    }
    private fun enviarLocalizacaoFirebase(latitude: Double, longitude: Double, position: Int) {
        // Restante da implementação para enviar a localização para o Firebase Firestore

        // Ação executada quando a gravação for bem-sucedida
        Log.d(TAG, "Localização enviada com sucesso para o Firebase Firestore!")

        // Importante: Limpar a variável currentPosition
        currentPosition = RecyclerView.NO_POSITION
    }



}
