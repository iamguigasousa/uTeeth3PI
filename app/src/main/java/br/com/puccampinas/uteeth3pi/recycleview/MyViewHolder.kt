package br.com.puccampinas.uteeth3pi.recycleview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.R

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv_name: TextView = itemView.findViewById(R.id.tv_name)
    val tv_phone: TextView = itemView.findViewById(R.id.tv_phone)
    val btn_aceitar: TextView = itemView.findViewById(R.id.btn_aceitar)
    val btn_recusar: TextView = itemView.findViewById(R.id.btn_recusar)





}
