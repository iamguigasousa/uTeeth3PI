package br.com.puccampinas.uteeth3pi.recycleview

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.R

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv_name: TextView = itemView.findViewById(R.id.tv_name)
    val tv_phone: TextView = itemView.findViewById(R.id.tv_phone)
    val tv_photo: ImageView = itemView.findViewById(R.id.tv_photo)
    val tv_photo1: ImageView = itemView.findViewById(R.id.tv_photo1)
    val tv_photo2: ImageView = itemView.findViewById(R.id.tv_photo2)
    val btn_aceitar: TextView = itemView.findViewById(R.id.btn_aceitar)
    val btn_recusar: TextView = itemView.findViewById(R.id.btn_recusar)



}
