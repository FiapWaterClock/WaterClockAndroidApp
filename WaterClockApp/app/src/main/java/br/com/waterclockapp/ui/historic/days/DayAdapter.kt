package br.com.waterclockapp.ui.historic.days

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.DayModel
import java.util.*

class DayAdapter(private val days: List<DayModel>) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day: DayModel = days[position]
        holder.bind(day)
        if(position == 0) {
            val margin: ViewGroup.MarginLayoutParams = holder.shapeCircle?.layoutParams as ViewGroup.MarginLayoutParams
            margin.setMargins(0, 50, 0, 0)
            holder.shapeCircle?.requestLayout()
            holder.shapeLine?.visibility = View.VISIBLE
        }
        if((days.size - 1) == position){
            holder.shapeLine?.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DayViewHolder(inflater, parent)
    }


    override fun getItemCount(): Int = days.size



    class DayViewHolder(inflater: LayoutInflater, parent: ViewGroup):
            RecyclerView.ViewHolder(inflater.inflate(R.layout.card_view_days, parent, false)){
        private var buttonValue: TextView? = null
        private var buttonVolume: TextView? = null
        private var textViewDay: TextView? = null
        private var textViewDescription: TextView? = null
        var shapeLine: View? = null
        var shapeCircle: View? = null

        init {
            buttonValue = itemView.findViewById(R.id.buttonValueHistoric)
            buttonVolume = itemView.findViewById(R.id.buttonVolumeHistoric)
            textViewDay = itemView.findViewById(R.id.textViewDateHistoric)
            textViewDescription = itemView.findViewById(R.id.textViewDescriptionHistoric)
            shapeLine = itemView.findViewById(R.id.shape_line)
            shapeCircle = itemView.findViewById(R.id.shape_circle)
        }

        @SuppressLint("SetTextI18n")
        fun bind(day : DayModel){
            textViewDay?.text = "${day.date.get(Calendar.DAY_OF_MONTH)}/${day.date.get(Calendar.MONTH)}"
            buttonValue?.text = "R$ ${day.value}"
            buttonVolume?.text = "${day.volume} m³"
            textViewDescription?.text = day.description
        }


    }

}
