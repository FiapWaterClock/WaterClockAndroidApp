package br.com.waterclockapp.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.ClockModel

class ClockAdapter(private val clocks: List<ClockModel>)  : RecyclerView.Adapter<ClockAdapter.ClockViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ClockViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int  = clocks.size

    override fun onBindViewHolder(holder: ClockViewHolder, position: Int) {
        val payment = clocks[position]
        holder.bind(payment)
    }


    class ClockViewHolder(inflater: LayoutInflater, parent: ViewGroup):
            RecyclerView.ViewHolder(inflater.inflate(R.layout.card_view_clock, parent, false)){

        private var clockDateInstallation : TextView? = null
        private var clockState : TextView? = null

        init {
            clockDateInstallation = itemView.findViewById(R.id.textViewClockInstallationDate)
            clockState = itemView.findViewById(R.id.textViewClockState)
        }

        fun bind(clock: ClockModel){
            clockDateInstallation?.text = "${clock.installationDate}"
            clockState?.text = if(clock.activate) "Activite" else  "Desactivite"
        }


    }
}