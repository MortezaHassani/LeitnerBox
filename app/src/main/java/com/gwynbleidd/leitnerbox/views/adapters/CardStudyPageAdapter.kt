package com.gwynbleidd.leitnerbox.views.adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.gwynbleidd.leitnerbox.R
import com.gwynbleidd.leitnerbox.database.entities.CardEntity
import com.gwynbleidd.leitnerbox.databinding.CardsStudyViewpagerItemsBinding
import com.gwynbleidd.leitnerbox.viewmodels.CardsViewModel
import com.gwynbleidd.leitnerbox.views.activities.CardsActivity

class CardStudyPageAdapter(private val activityContext: CardsActivity) :
    RecyclerView.Adapter<CardStudyPageAdapter.CardsGroupViewHolder>() {

    lateinit var binding: CardsStudyViewpagerItemsBinding

    lateinit var viewModel: CardsViewModel

    private val cardList: ArrayList<CardEntity> = arrayListOf()


    class CardsGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemsBinding: CardsStudyViewpagerItemsBinding? = DataBindingUtil.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsGroupViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.cards_study_viewpager_items,
            parent,
            false
        )

        viewModel = activityContext.viewModel



        return CardsGroupViewHolder(binding.root)
    }

    override fun getItemCount(): Int = cardList.size

    override fun onBindViewHolder(holder: CardsGroupViewHolder, position: Int) {

        holder.itemsBinding?.let { itemBinding ->

            itemBinding.context = activityContext
            itemBinding.cardEntity = cardList[position]
            cardStateListener(holder, position)

            itemBinding.cardLayout.setOnClickListener {
                if (cardList[position].isFront) {
                    holder.itemsBinding.cardWordText.text = cardList[position].front
                    holder.itemsBinding.okButton.visibility = View.INVISIBLE
                    holder.itemsBinding.oopsButton.visibility = View.INVISIBLE
                } else {
                    holder.itemsBinding.cardWordText.text = cardList[position].back
                    holder.itemsBinding.okButton.visibility = View.VISIBLE
                    holder.itemsBinding.oopsButton.visibility = View.VISIBLE
                }
                cardClickListener(it)
                Handler().postDelayed({
                    cardList[position].isFront = !cardList[position].isFront
                    viewModel.editCard(cardList[position].groupId,"showAnswer", cardList[position].isFront, cardList[position])
                    cardStateListener(holder, position)
                }, 300)
            }

            itemBinding.addToFav.setOnClickListener {
                cardList[position].isFav = !cardList[position].isFav
                viewModel.editCard(cardList[position].groupId,"makeFav", cardList[position].isFav, cardList[position])

                it as ImageView
                if (cardList[position].isFav) {
                    it.setImageResource(R.drawable.ic_star_black_24dp)
                } else {
                    it.setImageResource(R.drawable.ic_star_border_black_24dp)
                }
            }

            itemBinding.okButton.setOnClickListener {
                it as ImageView
                cardList[position].isDone = true
                viewModel.editCard(cardList[position].groupId,"makeDone", true, cardList[position])
                it.setImageResource(R.drawable.ic_thumbs_up_fill)
                itemBinding.oopsButton.setImageResource(R.drawable.ic_thumbs_down)
            }

            itemBinding.oopsButton.setOnClickListener {
                it as ImageView
                cardList[position].isDone = false
                viewModel.editCard(cardList[position].groupId,"makeDone", false, cardList[position])
                it.setImageResource(R.drawable.ic_thumbs_down_fill)
                itemBinding.okButton.setImageResource(R.drawable.ic_thumbs_up)
            }

        }
    }

    fun add(items: List<CardEntity>, size: Int) {
        val previousDataSize: Int = this.itemCount
        if (previousDataSize < size) {
            if (previousDataSize == 0) {
                cardList.addAll(items)
                notifyItemInserted(1)
            } else {
                cardList.addAll(items)
                notifyItemRangeInserted(previousDataSize, items.size)
            }
        }
    }

    fun clearAdd(items: List<CardEntity>) {
        cleanAdapter()
        cardList.addAll(items)
        notifyItemInserted(1)
    }

    private fun cleanAdapter() {
        cardList.clear()
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        cardList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    private fun cardClickListener(view: View) {
        view.animate()
            .scaleX(0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .duration = 300

        Handler().postDelayed({
            view.animate()
                .scaleX(1f)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .duration = 300
        }, 300)
    }

    private fun cardStateListener(holder: CardsGroupViewHolder, position: Int) {
        holder.itemsBinding?.let {
            if (cardList[position].isFront) {
                holder.itemsBinding.cardWordText.text = cardList[position].front
                holder.itemsBinding.okButton.visibility = View.INVISIBLE
                holder.itemsBinding.oopsButton.visibility = View.INVISIBLE
            } else {
                holder.itemsBinding.cardWordText.text = cardList[position].back
                holder.itemsBinding.okButton.visibility = View.VISIBLE
                holder.itemsBinding.oopsButton.visibility = View.VISIBLE
                if (cardList[position].isDone != null) {
                    if (cardList[position].isDone!!) {
                        holder.itemsBinding.okButton.setImageResource(R.drawable.ic_thumbs_up_fill)
                        holder.itemsBinding.oopsButton.setImageResource(R.drawable.ic_thumbs_down)
                    } else {
                        holder.itemsBinding.okButton.setImageResource(R.drawable.ic_thumbs_up)
                        holder.itemsBinding.oopsButton.setImageResource(R.drawable.ic_thumbs_down_fill)
                    }
                } else {
                    holder.itemsBinding.okButton.setImageResource(R.drawable.ic_thumbs_up)
                    holder.itemsBinding.oopsButton.setImageResource(R.drawable.ic_thumbs_down)
                }
            }
            if (cardList[position].isFav) {
                holder.itemsBinding.addToFav.setImageResource(R.drawable.ic_star_black_24dp)
            } else {
                holder.itemsBinding.addToFav.setImageResource(R.drawable.ic_star_border_black_24dp)
            }
        }
    }

}