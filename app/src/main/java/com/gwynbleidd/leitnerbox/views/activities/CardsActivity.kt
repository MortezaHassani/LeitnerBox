package com.gwynbleidd.leitnerbox.views.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.gwynbleidd.leitnerbox.R
import com.gwynbleidd.leitnerbox.database.entities.CardEntity
import com.gwynbleidd.leitnerbox.database.entities.CardGroupEntity
import com.gwynbleidd.leitnerbox.databinding.CardsStudyActivityBinding
import com.gwynbleidd.leitnerbox.di.modules.CardSModule
import com.gwynbleidd.leitnerbox.utilites.HorizontalMarginItemDecoration
import com.gwynbleidd.leitnerbox.utilites.showToast
import com.gwynbleidd.leitnerbox.viewmodels.CardsViewModel
import com.gwynbleidd.leitnerbox.views.adapters.CardStudyPageAdapter
import com.gwynbleidd.leitnerbox.views.app.LeitnerApp
import javax.inject.Inject
import kotlin.math.abs

class CardsActivity : AppCompatActivity() {

    lateinit var binding: CardsStudyActivityBinding


    @Inject
    lateinit var adapter: CardStudyPageAdapter

    @Inject
    lateinit var viewModel: CardsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        application?.let {
            it as LeitnerApp
            it.provideAppComponent()
                .injectCardsActivity()
                .setContext(CardSModule(this))
                .build()
                .inject(this)
        }


        val backColor: Int = intent.getIntExtra("COLOR_ID", R.color.colorPrimary)
        val cardsDeck: Int = intent.getIntExtra("CARD_DECK",0)
        val groupId: Long = intent.getLongExtra("GROUP_ID", 0L)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, backColor)
        }


        binding = DataBindingUtil.setContentView(this, R.layout.cards_study_activity)
        binding.lifecycleOwner = this

        viewModel.getCardsOfDeck(groupId ,cardsDeck).observe(this, Observer {
            if (it.isNotEmpty())
                adapter.add(it, viewModel.getDeckSize(groupId,cardsDeck))
        })

        /* viewModel.cardsLiveData.observe(this, Observer {
             if (it.isNotEmpty())
                 adapter.add(it, viewModel.cardBoxSize)
         })*/

        val cardList: MutableList<CardEntity> = mutableListOf()
        viewModel.getCardsOfDeck(groupId,cardsDeck).observe(this, Observer {
            cardList.addAll(it)
            viewModel.addCard(groupId,cardList)
        })


        /* for (i in 0..10){
             val card = CardEntity(id = i, front = "Front #$i", back = "Back #$i", deck = 0)
             cardList.add(card)
         }
        viewModel.addCard(cardList)*/

        /*val card1 = CardEntity(id = 0, front = "Front #1", back = "Back #1", deck = 10)
        val card2 = CardEntity(id = 1, front = "Front #2", back = "Back #2", deck = 10)
        val card3 = CardEntity(id = 2, front = "Front #3", back = "Back #3", deck = 10)
        val card4 = CardEntity(id = 3, front = "Front #4", back = "Back #4", deck = 10)
        val card5 = CardEntity(id = 4, front = "Front #5", back = "Back #5", deck = 10)*/




        binding.cardActivityDone.setOnClickListener {
            viewModel.donDaysWord(groupId,cardsDeck)
            binding.cardActivityDoneLoading.visibility = View.VISIBLE
        }

        viewModel.doneTheWorkLiveData.observe(this, Observer {
            binding.cardActivityDoneLoading.visibility = View.GONE
            if (it) {
                this.finish()
            } else {
                showToast("Error!")
            }
        })



        binding.color = backColor
        binding.context = this

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        binding.cardActivityCards.setPageTransformer(pageTransformer)

// The ItemDecoration gives the current (centered) item horizontal margin so that
// it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.cardActivityCards.addItemDecoration(itemDecoration)

        binding.cardActivityCards.offscreenPageLimit = 1

        adapter = CardStudyPageAdapter(this)

        /*val cardsList: List<CardEntity> = listOf(card1, card2, card3, card4, card5)
        adapter.add(cardsList)*/

        binding.cardActivityCards.adapter = adapter


    }

    fun closeActivity(view: View) {
        this.finish()
    }

}