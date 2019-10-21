package br.com.waterclockapp.ui.historic.days

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.util.LitersToMoney
import org.threeten.bp.LocalDate

class DayAdapter(private val days: List<ConsumptionModel>, private val month: ConsumptionModel) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day: ConsumptionModel = days[position]
        holder.bind(day, month)
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
        fun bind(day: ConsumptionModel, month: ConsumptionModel){
            val localDate = LocalDate.parse(day.time)
            textViewDay?.text = "${localDate.dayOfMonth}/${localDate.monthValue}"
            buttonValue?.text = LitersToMoney.convertDayToMoney(day.litersPerMinute, month.litersPerMinute)
            buttonVolume?.text = "${(day.litersPerMinute/1000).toString().replace(".",",")} m³"
            textViewDescription?.text = "Consumption of the $localDate"
        }


    }

}
