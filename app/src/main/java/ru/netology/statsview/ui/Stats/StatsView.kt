package ru.netology.statsview.ui.Stats

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.netology.statsview.R
import ru.netology.statsview.ui.Stats.utils.AndroidUtils
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null, //набор атрибутов, которые можно передать через xml
    defStyleAttr: Int = 0, //стиль атрибута по умолчанию
    defStyleRes: Int = 0 //стиль по умолчанию
) : View(
    context,
    attributeSet,
    defStyleAttr,
    defStyleRes
) {

    private var textSize = AndroidUtils.dp(context, 20).toFloat()
    private var lineWidth = AndroidUtils.dp(context, 5)
    private var colors = emptyList<Int>()

    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth.toFloat()).toInt()

            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor())
            )
        }
    }

    private var radius = 0F

    //процент загрузки
    var data: List<Float> = emptyList()
        set(value) {
            field = value
            //провоцируем вызов функци onDraw()
            invalidate()
        }

    //создаем прямоугольник, который нужен ля отрисовки дуги
    var oval = RectF()

    //точка центра, для отрисовки окружности относительно ее
    private var center = PointF()

    private val paint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        strokeWidth = lineWidth.toFloat()
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    //кисть для отрисовки текста
    private val textPaint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        textSize = this@StatsView.textSize
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth
        val x = w / 2F
        val y = h / 2F
        center = PointF(x, y)
        //заполянем прямоугольник данным при изменении размеров
        oval = RectF(
            //расчет размеров левой грани
            center.x - radius,
            //рассчет верхней грани
            center.y - radius,
            //рассчет правой грани
            center.x + radius,
            //рассчет правой грани
            center.y + radius
        )
    }

    //обрабатываем данные
    override fun onDraw(canvas: Canvas) {
        //если список пустой, используем ранний выход, чтобы не тратить ресурсы на отрисовку
        if (data.isEmpty()) {
            return
        }

        //определим стартовый угол поворота
        var startAngle = -90F

        //объявляем переменную суммы всех значений data
        var sumData = 0F

        //суммируем все значения списка для определения процентов
        data.forEach {
            sumData += it
        }

        //обойдем список элементов и рассчитаем угол поворота каждого
        data.forEachIndexed {index, datum ->
            var datumPercent = datum / sumData

            var angle = datumPercent * 360F

            //назначим каждому элементу свой цвет
            //рассчитываем случайный цвет, который находится от черного до белого
            paint.color = colors.getOrElse(index) { generateRandomColor() }

            //отрисовываем дугу
            canvas.drawArc(oval, startAngle, angle, false, paint)

            //чтобы не рисовать на одном месте добавим отступ к стартавому углу поворота
            startAngle += angle
        }

        //отрисовка текста
        canvas.drawText(
            //сумма всех элементов умноженная на 100
            "%.2f%%".format(sumData/20),
            center.x,
            center.y + textPaint.textSize / 4, //корректировочный к-т
            textPaint
        )
    }

    private fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}