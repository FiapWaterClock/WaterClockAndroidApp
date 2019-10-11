package br.com.waterclockapp.ui.historic

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import br.com.waterclockapp.R
import br.com.waterclockapp.ui.historic.days.DaysFragment
import br.com.waterclockapp.ui.historic.month.MonthFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_historic.*
import java.text.SimpleDateFormat
import java.util.*


class HistoricFragment : Fragment() {

    private var year: Int = 0
    private var month: Int = 0
    private var textMonth = ""
    private var calendar : Calendar = Calendar.getInstance()
    private lateinit var fragment : Fragment
    private var positionTabLayout = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verifyMonth()
        month = calendar.get(Calendar.MONTH + 1)
        year = calendar.get(Calendar.YEAR)
        actionTabSelected()
        actionArrow()
        positionFragment()
    }

    private fun actionArrow() {
        val thisMonth = calendar.get(Calendar.MONTH)

        var contador = 6
        setTextDate()
        rightImageView.visibility = View.INVISIBLE

        leftImageView.setOnClickListener {
            if(contador >= 0){
                contador--
                calendar.add(Calendar.MONTH, -1)
                setTextDate()
                leftImageView.visibility = View.VISIBLE
                rightImageView.visibility = View.VISIBLE
                positionFragment()
            } else {
                leftImageView.visibility = View.INVISIBLE
                positionFragment()
            }

            if(contador == 0)
                leftImageView.visibility = View.INVISIBLE

        }

        rightImageView.setOnClickListener {
            if(month <= thisMonth) {
                calendar.add(Calendar.MONTH, +1)
                contador++
                setTextDate()
                leftImageView.visibility = View.VISIBLE
                rightImageView.visibility = View.VISIBLE
                positionFragment()
            }else {
                rightImageView.visibility = View.INVISIBLE
                positionFragment()
            }
            if(month == thisMonth + 1) {
                rightImageView.visibility = View.INVISIBLE
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setTextDate() {
        monthTextView.text = SimpleDateFormat("MMM").format(calendar.time).toUpperCase()
        yearTextView.text = calendar.get(Calendar.YEAR).toString()
        month = calendar.get(Calendar.MONTH) + 1
        year = calendar.get(Calendar.YEAR)
    }

    private fun actionTabSelected() {
        historicLabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    positionTabLayout = tab.position
                    positionFragment()
                }
            }
        })
    }

    private fun positionFragment() {
        if(month == 0 && year == 0){
            month = calendar.get(Calendar.MONTH) + 1
            year = calendar.get(Calendar.YEAR)
        }

        when (positionTabLayout) {
            0 -> fragment = MonthFragment(month, year)
            1 -> fragment = DaysFragment(month, year)
        }
        loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager : FragmentManager? = activity?.supportFragmentManager
        val fragmentTransition = fragmentManager?.beginTransaction()
        fragmentTransition?.replace(R.id.historicFrameLayout, fragment)
        fragmentTransition?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransition?.commit()
    }

    fun getDateSetFragment(m: Int, y : Int, textMonth: String){
        month = m
        year = y
        this.textMonth = textMonth
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historic, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    private fun verifyMonth() {
        if(textMonth.isEmpty())
            textMonth = SimpleDateFormat("MMM").format(calendar.time)
    }




}
