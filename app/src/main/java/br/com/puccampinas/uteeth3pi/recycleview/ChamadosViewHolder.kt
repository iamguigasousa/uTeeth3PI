package br.com.puccampinas.uteeth3pi.recycleview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.R

class ChamadosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv_name: TextView = itemView.findViewById(R.id.tv_name)
    val tv_phone: TextView = itemView.findViewById(R.id.tv_phone)
    val btn_localizacao: TextView = itemView.findViewById(R.id.btn_localizacao)
    val latitude: String = toString()
    val longitude: String = toString()



}
