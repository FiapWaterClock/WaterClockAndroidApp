package br.com.waterclockapp.util

object LitersToMoney {

    fun convertLitersToMoney(liters: Double) :String{
        val taxa = verifyTaxaMonth(liters)
        if(taxa == 0.0) return "R$ 8,88"
        return "R$ ${(taxa * liters)}"
    }

    fun verifyTaxaMonth(liters: Double) : Double{
        val meters = liters / 1000
        return when {
            meters < 11 -> 0.0
            meters < 21 -> 1.53
            meters < 31 -> 5.43
            meters < 51 -> 7.74
            else -> 8.55
        }
    }

    fun convertDayToMoney(dayLiters: Double, liters: Double):String{
        val taxa = verifyTaxaMonth(liters)
        if(taxa == 0.0) return "R$ 8,88"
        return "R$ ${(taxa * dayLiters)}"
    }
}