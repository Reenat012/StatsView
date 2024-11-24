package ru.netology.statsview.ui.Stats.utils

import android.content.Context
import kotlin.math.ceil

object  AndroidUtils {
    //функция для расчета размеров
    fun dp(context: Context, dp: Int) : Int =
        ceil(context.resources.displayMetrics.density * dp).toInt()
}