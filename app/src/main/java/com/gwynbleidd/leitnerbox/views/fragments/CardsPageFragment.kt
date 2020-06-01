package com.gwynbleidd.leitnerbox.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.gwynbleidd.leitnerbox.R
import com.gwynbleidd.leitnerbox.database.entities.CardGroupEntity
import com.gwynbleidd.leitnerbox.databinding.CardsFragmentBinding
import com.gwynbleidd.leitnerbox.di.modules.CardSModule
import com.gwynbleidd.leitnerbox.di.modules.CardsFragmentModule
import com.gwynbleidd.leitnerbox.utilites.HorizontalMarginItemDecoration
import com.gwynbleidd.leitnerbox.viewmodels.CardsViewModel
import com.gwynbleidd.leitnerbox.views.activities.HomeActivity
import com.gwynbleidd.leitnerbox.views.adapters.CardGroupPageAdapter
import com.gwynbleidd.leitnerbox.views.app.LeitnerApp
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

class CardsPageFragment : Fragment() {


    lateinit var binding: CardsFragmentBinding


    //    private lateinit var adapter : CardsGroupAdapter
    private lateinit var adapter: CardGroupPageAdapter

    @Inject
    lateinit var viewModel: CardsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val groupList: ArrayList<CardGroupEntity> = arrayListOf()
//        for (i in 0..30) {

//        }




        /*val group1 = CardGroupEntity("Deck #1", R.color.colorPrimaryDark, deck = 10)
        val group2 = CardGroupEntity("Deck #2", R.color.favButton, deck = 20)
        val group3 = CardGroupEntity("Deck #3", R.color.colorAccent, deck = 30)


        val groupList = arrayListOf(group1, group2, group3)*/

        activity?.application?.let {
            it as LeitnerApp
            it.provideAppComponent()
                .injectCardsFragment()
                .setContext(CardsFragmentModule(activity as HomeActivity))
                .build()
                .inject(this)
        }

        if(viewModel.groupBoxSize==0){
            val group = CardGroupEntity(name = "Deck #$0", color = R.color.colorPrimaryDark, deck = 0)
            groupList.add(group)
            val group1 = CardGroupEntity(name = "Deck #$1", color = R.color.colorAccent, deck = 1)
            groupList.add(group1)
            val addGroup = CardGroupEntity(name = "DeckAdd", color = R.color.colorPrimaryDark, deck = 1000)
            groupList.add(addGroup)

        }

        if (viewModel.cardBoxSize == 0) {
            viewModel.addNewCards(groupList[0].Id)
        }


        val sortedList = groupList.sortedWith(compareBy {it.deck})

        adapter = CardGroupPageAdapter(activity as HomeActivity, viewModel, this)
        adapter.add(sortedList)


        //data binding
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.cards_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*// Disable clip to padding
        viewPager.setClipToPadding(false);
        // set padding manually, the more you set the padding the more you see of prev & next page
        viewPager.setPadding(40, 0, 40, 0);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        viewPager.setPageMargin(20);*/


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
        binding.cardsGroupPager.setPageTransformer(pageTransformer)

// The ItemDecoration gives the current (centered) item horizontal margin so that
// it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            context!!,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.cardsGroupPager.addItemDecoration(itemDecoration)

        binding.cardsGroupPager.offscreenPageLimit = 1

        binding.cardsGroupPager.adapter = adapter
    }

    override fun onResume() {
        Timber.tag("fragment").d("resume ")
        super.onResume()

    }
}