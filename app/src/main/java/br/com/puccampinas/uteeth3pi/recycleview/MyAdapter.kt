package br.com.puccampinas.uteeth3pi.recycleview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.MainActivity
import br.com.puccampinas.uteeth3pi.R
import br.com.puccampinas.uteeth3pi.recycleview.MyAdapter.MyViewHolder

class MyAdapter(var context: Context, var userArrayList: ArrayList<User>) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = userArrayList[position]
        holder.name.text = user.name
        holder.phone.text = user.phone.toString()
    }

    override fun getItemCount(): Int {
        return userArrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var phone: TextView
        var btn_aceitar: TextView
        var btn_recusar: TextView

        init {
            name = itemView.findViewById(R.id.tvfirstName)
            phone = itemView.findViewById(R.id.tvphone)
            btn_aceitar = itemView.findViewById(R.id.btn_aceitar)
            btn_recusar = itemView.findViewById(R.id.btn_recusar)
            btn_aceitar.setOnClickListener { view ->
                val intent = Intent(view.context, MainActivity::class.java)
                view.context.startActivity(intent)
            }
            btn_recusar.setOnClickListener { view ->
                val intent = Intent(view.context, MainActivity::class.java)
                view.context.startActivity(intent)
            }
        }
    }
}