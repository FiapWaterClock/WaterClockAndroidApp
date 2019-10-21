package br.com.waterclockapp.ui.clock


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.waterclockapp.R
import br.com.waterclockapp.ui.home.HomeContract

/**
 * A simple [Fragment] subclass.
 */
class ClockFragment(val viewHome: HomeContract.View) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clock, container, false)
    }


}
