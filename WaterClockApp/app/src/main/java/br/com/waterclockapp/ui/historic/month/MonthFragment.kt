package br.com.waterclockapp.ui.historic.month


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.waterclockapp.data.model.ConsumptionModel

import br.com.waterclockapp.data.model.DayModel
import br.com.waterclockapp.ui.historic.HistoricContract
import br.com.waterclockapp.ui.historic.HistoricPresenter
import br.com.waterclockapp.ui.login.LoginActivity
import br.com.waterclockapp.util.Preferences
import kotlinx.android.synthetic.main.fragment_month.*
import lecho.lib.hellocharts.model.*
import java.util.*
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener


class MonthFragment(var month: Int, var year: Int) : Fragment(), HistoricContract.View {

    private lateinit var presenter: HistoricContract.Presenter

    private var yAxisValues = mutableListOf<PointValue>()
    private var daysModel = mutableListOf<DayModel>()
    private var axisValues = mutableListOf<AxisValue>()

    private val days = listOf(
            DayModel(10.2 ,20.34, GregorianCalendar(2019, 10, 10), "Teste de descricao"),
            DayModel(11.2 ,30.34, GregorianCalendar(2019, 10, 11), "Teste de descricao"),
            DayModel(12.2 ,40.34, GregorianCalendar(2019, 10, 12), "Teste de descricao"),
            DayModel(13.2 ,50.34, GregorianCalendar(2019, 10, 13), "Teste de descricao"),
            DayModel(14.2 ,60.0, GregorianCalendar(2019, 10, 14), "Teste de descricao"),
            DayModel(15.2 ,23.34, GregorianCalendar(2019, 10, 15), "Teste de descricao"),
            DayModel(16.2 ,28.34, GregorianCalendar(2019, 10, 16), "Teste de descricao"),
            DayModel(17.2 ,10.34, GregorianCalendar(2019, 10, 17), "Teste de descricao"),
            DayModel(18.2 ,0.44, GregorianCalendar(2019, 10, 18), "Teste de descricao"),
            DayModel(13.2 ,77.44, GregorianCalendar(2019, 10, 19), "Teste de descricao"),
            DayModel(12.2 ,54.44, GregorianCalendar(2019, 10, 20), "Teste de descricao"),
            DayModel(13.2 ,23.44, GregorianCalendar(2019, 10, 21), "Teste de descricao"),
            DayModel(184.2 ,7.44, GregorianCalendar(2019, 10, 22), "Teste de descricao"),
            DayModel(183.2 ,56.44, GregorianCalendar(2019, 10, 23), "Teste de descricao")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(br.com.waterclockapp.R.layout.fragment_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = HistoricPresenter(this)
        presenter.getConsumptionMonth(month, year)
        startGraph(days)
    }

    private fun startGraph(daysList: List<DayModel>) {


        for (i in (daysList.indices)){
            val element = AxisValue(i.toFloat()).setLabel(daysList[i].date.get(Calendar.DAY_OF_MONTH).toString())
            axisValues.add(i, element)
            daysModel.add(i, daysList[i])
            yAxisValues.add(i, PointValue(i.toFloat(), daysList[i].volume.toFloat()))
        }

        val line = Line().apply {
            values = yAxisValues
            isCubic = false
            color = Color.parseColor("#FFA726") }


        val lines = mutableListOf<Line>()
        lines.add(line)

        val lineChartData = LineChartData()
        lineChartData.lines = lines


        graphMonthDays.isInteractive = true
        graphMonthDays.zoomType = ZoomType.HORIZONTAL
        graphMonthDays.maxZoom = 1.toFloat()//Maximum method scale
        graphMonthDays.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)
        graphMonthDays.lineChartData = lineChartData

        val axisX = Axis()
        axisX.values = axisValues
        axisX.textSize = 10
        axisX.textColor = Color.parseColor("#FFFFFF")
        lineChartData.axisXBottom = axisX

        /*val axisY = Axis()
        axisY.textColor = Color.parseColor("#FFFFFF")
        axisY.textSize = 16
        lineChartData.axisYLeft = axisY
*/

        val viewport = Viewport(graphMonthDays.maximumViewport)
        //viewport.top = 100F

        viewport.top = viewport.top + 4
        viewport.bottom = viewport.bottom - 8
        graphMonthDays.maximumViewport = viewport
        graphMonthDays.currentViewport = viewport
        graphMonthDays.isViewportCalculationEnabled = false

        graphMonthDays.onValueTouchListener = object : LineChartOnValueSelectListener {
            @SuppressLint("ShowToast")
            override fun onValueSelected(lineIndex: Int, pointIndex: Int, value: PointValue?) {
                textViewValueMonthDay.text = "${daysModel[pointIndex].value}"
                textViewVolumeMonthDay.text = "${daysModel[pointIndex].volume}"
            }

            override fun onValueDeselected() {

            }

        }


    }

    override fun notification(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun initInformations(models: List<ConsumptionModel>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout() {
        val intentHome = Intent(activity, LoginActivity::class.java)
        Preferences.clearPreferences()
        startActivity(intentHome)
        activity?.finish()    }


}
