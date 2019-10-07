package br.com.waterclockapp.ui.payment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.PaymentModel
import br.com.waterclockapp.data.model.PaymentStatus
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : Fragment() {

    private val payments = listOf(
            PaymentModel("10/2019", PaymentStatus.PAGO),
            PaymentModel("9/2019", PaymentStatus.PAGO),
            PaymentModel("8/2019", PaymentStatus.PAGO),
            PaymentModel("7/2019", PaymentStatus.PAGO),
            PaymentModel("6/2019", PaymentStatus.PAGO),
            PaymentModel("5/2019", PaymentStatus.PAGO),
            PaymentModel("4/2019", PaymentStatus.PAGO),
            PaymentModel("3/2019", PaymentStatus.PAGO),
            PaymentModel("2/2019", PaymentStatus.PAGO)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerPayment.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = PaymentAdapter(payments)
        }

    }


}
