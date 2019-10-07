package br.com.waterclockapp.ui.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.PaymentModel
import br.com.waterclockapp.ui.historic.days.DayAdapter

class PaymentAdapter(var payments: List<PaymentModel>) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PaymentViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = payments.size

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = payments[position]
        holder.bind(payment)
    }


    class PaymentViewHolder(inflater: LayoutInflater, parent: ViewGroup):
            RecyclerView.ViewHolder(inflater.inflate(R.layout.card_view_payment, parent, false)){

        private var paymentDescription : TextView? = null
        private var paymentStatus : TextView? = null

        init {
            paymentDescription = itemView.findViewById(R.id.textViewPaymentDescription)
            paymentStatus = itemView.findViewById(R.id.textViewPaymentStatus)
        }

        fun bind(payment: PaymentModel){
            paymentDescription?.text = "Boleto ${payment.date}"
            paymentStatus?.text = payment.status.name
        }
    }
}
