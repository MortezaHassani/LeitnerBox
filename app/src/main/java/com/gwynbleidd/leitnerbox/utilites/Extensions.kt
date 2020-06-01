package com.gwynbleidd.leitnerbox.utilites

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.gwynbleidd.leitnerbox.R

fun Activity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun PagerAdapter.showToast(context: Context,text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun View.slideRight(duration: Int = 500, delay: Long = 250) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(-this.width.toFloat(), 0f, 0f, 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    animate.startOffset = delay
    this.startAnimation(animate)
}

fun View.slideDown(duration: Int = 500, delay: Long = 250) {
    this.visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, -200f, 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

fun View.slideUp(duration: Int = 500, delay: Long = 250) {
    this.visibility = View.INVISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, -200f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}


fun RelativeLayout.select(context: Context) {
    this.forEach {
        when (it) {
            is ImageView -> {
                it.setColorFilter(ContextCompat.getColor(context, R.color.iconSelect))
            }
            is TextView -> {
                it.setTextColor(ContextCompat.getColor(context, R.color.iconSelect))
            }
        }
    }
}

fun RelativeLayout.unSelect(context: Context) {
    this.forEach {
        when (it) {
            is ImageView -> {
                it.setColorFilter(ContextCompat.getColor(context, R.color.iconUnSelect))
            }
            is TextView -> {
                it.setTextColor(ContextCompat.getColor(context, R.color.iconUnSelect))
            }
        }
    }
}
