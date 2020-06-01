package com.gwynbleidd.leitnerbox.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.gwynbleidd.leitnerbox.R
import com.gwynbleidd.leitnerbox.databinding.HomeActivityBinding
import com.gwynbleidd.leitnerbox.utilites.select
import com.gwynbleidd.leitnerbox.utilites.unSelect
import com.gwynbleidd.leitnerbox.views.fragments.CardsPageFragment

class HomeActivity : AppCompatActivity() {

    lateinit var binding: HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        binding.main = this

        selectIcon("cardsIcon")

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_load,CardsPageFragment())
            .show(CardsPageFragment())
            .commitNow()

        /*binding.test.setOnClickListener {
            it.animate()
                .scaleX(0f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .duration = 300

            Handler().postDelayed({
                it.animate()
                    .scaleX(1f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .duration = 300

                it as TextView
                if (it.text == "foreground")
                    it.text = "background"
                else
                    it.text = "foreground"
            }, 300)


        }*/


        /*binding.cardsIcon.setOnClickListener {
            selectIcon("cardsIcon")
        }

        binding.statsIcon.setOnClickListener {
            selectIcon("statsIcon")
        }

        binding.favIcon.setOnClickListener {
            selectIcon("favIcon")
        }

        binding.settingsIcon.setOnClickListener {
            selectIcon("settingsIcon")
        }*/


        /* Handler().postDelayed({
             binding.startButton.slideRight(duration = 300,delay = 0L)
             binding.settingButton.slideRight(duration = 300,delay = 150L)
             binding.favButton.slideRight(duration = 300,delay = 300L)
         }, 1000)*/
    }

    fun selectIcon(iconName: String) {
        when (iconName) {
            "cardsIcon" -> {
                binding.cardsIcon.select(this)
                binding.statsIcon.unSelect(this)
                binding.favIcon.unSelect(this)
                binding.settingsIcon.unSelect(this)
                supportFragmentManager
                    .beginTransaction()
                    .show(CardsPageFragment())
                    .commitNow()

            }

            "statsIcon" -> {
                binding.cardsIcon.unSelect(this)
                binding.statsIcon.select(this)
                binding.favIcon.unSelect(this)
                binding.settingsIcon.unSelect(this)
                supportFragmentManager
                    .beginTransaction()
                    .hide(CardsPageFragment())
                    .commitNow()
            }

            "favIcon" -> {
                binding.cardsIcon.unSelect(this)
                binding.statsIcon.unSelect(this)
                binding.favIcon.select(this)
                binding.settingsIcon.unSelect(this)
                supportFragmentManager
                    .beginTransaction()
                    .hide(CardsPageFragment())
                    .commitNow()
            }

            "settingsIcon" -> {
                binding.cardsIcon.unSelect(this)
                binding.statsIcon.unSelect(this)
                binding.favIcon.unSelect(this)
                binding.settingsIcon.select(this)
                supportFragmentManager
                    .beginTransaction()
                    .hide(CardsPageFragment())
                    .commitNow()
            }
        }
    }

}
