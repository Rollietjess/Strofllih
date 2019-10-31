package ie.wit.hillforts.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.hillforts.R
import ie.wit.hillforts.helpers.readImageFromPath
import ie.wit.hillforts.models.HillfortsModel
import kotlinx.android.synthetic.main.card_hillfort.view.*

interface HillfortsListener {
    fun onHillfortsClick(hillfort: HillfortsModel)
}

class HillfortAdapter constructor(private var hillforts: List<HillfortsModel>,
                                   private val listener: HillfortsListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_hillfort,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: HillfortsModel,  listener : HillfortsListener) {
            itemView.hillfortTitle.text = hillfort.title
            itemView.description.text = hillfort.description
            if(hillfort.dateVisited != ""){
                itemView.date_visited.text = hillfort.dateVisited
            }
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.image))
            itemView.setOnClickListener { listener.onHillfortsClick(hillfort) }
        }
    }
}