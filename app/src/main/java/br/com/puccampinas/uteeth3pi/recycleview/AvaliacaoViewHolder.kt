package br.com.puccampinas.uteeth3pi.recycleview

import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView




class AvaliacaoViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
    val tv_Rating: RatingBar = itemView.findViewById(br.com.puccampinas.uteeth3pi.R.id.ratingbar)
    val tv_Estrela: TextView = itemView.findViewById(br.com.puccampinas.uteeth3pi.R.id.tv_estrela)
    val tv_Nome : TextView = itemView.findViewById(br.com.puccampinas.uteeth3pi.R.id.tv_nome_avaliacao)
    val tv_Data : TextView = itemView.findViewById(br.com.puccampinas.uteeth3pi.R.id.tv_data_avaliacao)
    val tv_Avaliacao : TextView = itemView.findViewById(br.com.puccampinas.uteeth3pi.R.id.tv_comentario)

}