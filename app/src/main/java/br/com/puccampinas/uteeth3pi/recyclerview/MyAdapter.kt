import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.puccampinas.uteeth3pi.CustomResponse
import br.com.puccampinas.uteeth3pi.NotificacaoTesteView
import br.com.puccampinas.uteeth3pi.R


class MyAdapter(private val userList: ArrayList<NotificacaoTesteView>) :RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvUid : TextView = itemView.findViewById(R.id.tv_uid)
        val tvImage: TextView = itemView.findViewById(R.id.tv_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_view, parent, false)
        return MyViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvUid.text = userList[position].image
        holder.tvImage.text = userList[position].uidEmergency
    }
    override fun getItemCount(): Int {
        return userList.size

    }
}




