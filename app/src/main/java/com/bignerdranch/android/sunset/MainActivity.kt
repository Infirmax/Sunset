package com.bignerdranch.android.sunset

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bignerdranch.android.sunset.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isSunset = true


    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scene.setOnClickListener {
            if (isSunset) {
                startAnimation() // Execute sunset animation
            } else {
                reverseAnimation() // Execute sunrise (reverse) animation
            }

            isSunset = !isSunset
        }

    }


    private fun startAnimation(): Animator {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.height.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)
        animatorSet.start()

        return animatorSet
    }

    private fun reverseAnimation(): Animator {
        val sunYEnd = binding.sun.top.toFloat()
        val sunYStart = binding.sky.height.toFloat()

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunriseSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", nightSkyColor, sunsetSkyColor)
            .setDuration(1500)
        sunriseSkyAnimator.setEvaluator(ArgbEvaluator())

        val daySkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(1500)
        daySkyAnimator.setStartDelay(1500)
        daySkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunriseSkyAnimator)
            .with(daySkyAnimator)
        animatorSet.start()

        return animatorSet
    }



}
