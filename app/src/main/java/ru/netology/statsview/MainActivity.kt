package ru.netology.statsview

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.statsview.ui.Stats.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //получаем доступ к view по id
        //заполняем view данными
        val view = findViewById<StatsView>(R.id.statsView)
        view.data = listOf(
            500F,
            500F,
            500F,
            500F
        )

        val textAnim = findViewById<TextView>(R.id.label)

        view.animate()
            .rotation(360F)
            .setInterpolator(LinearInterpolator())
            .start()

        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    textAnim.text = "onAnimationStart"
                }

                override fun onAnimationEnd(p0: Animation?) {
                    textAnim.text = "onAnimationEnd"
                }

                override fun onAnimationRepeat(p0: Animation?) {
                    textAnim.text = "onAnimationRepeat"
                }

            })
        })
    }
}