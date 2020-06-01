package com.gwynbleidd.leitnerbox.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gwynbleidd.leitnerbox.R
import com.gwynbleidd.leitnerbox.database.entities.CardGroupEntity
import com.gwynbleidd.leitnerbox.databinding.AddNewGroupBinding
import com.gwynbleidd.leitnerbox.databinding.CardsViewpagerItemsBinding
import com.gwynbleidd.leitnerbox.utilites.slideDown
import com.gwynbleidd.leitnerbox.utilites.slideUp
import com.gwynbleidd.leitnerbox.viewmodels.CardsViewModel
import com.gwynbleidd.leitnerbox.views.activities.CardsActivity
import com.gwynbleidd.leitnerbox.views.fragments.CardsPageFragment

class CardGroupPageAdapter(
    private val activityContext: Context,
    private val viewModel: CardsViewModel,
    private val fragment: CardsPageFragment
) : RecyclerView.Adapter<CardGroupPageAdapter.CardsGroupViewHolder>() {

   /* lateinit var binding: CardsViewpagerItemsBinding
    lateinit var bindingAdd: AddNewGroupBinding*/

    private val cardGroupList: ArrayList<CardGroupEntity> = arrayListOf()


    class CardsGroupViewHolder : ViewHolder {
//        val itemsBinding: CardsViewpagerItemsBinding? = DataBindingUtil.bind(itemView)
//        val itemsBindingAdd: AddNewGroupBinding? = DataBindingUtil.bind(itemView)

        var itemsBinding: CardsViewpagerItemsBinding? = null
        var itemsBindingAdd: AddNewGroupBinding? = null

        constructor(binding: CardsViewpagerItemsBinding) : super(binding.root) {
            itemsBinding = binding
        }

        constructor(binding: AddNewGroupBinding) : super(binding.root) {
            itemsBindingAdd = binding
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            cardGroupList.size - 1 -> {
                0
            }
            else -> {
                1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsGroupViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val bindingAdd: AddNewGroupBinding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.add_new_group,
                    parent,
                    false
                )
                CardsGroupViewHolder(bindingAdd)
            }
            else -> {
                val binding: CardsViewpagerItemsBinding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.cards_viewpager_items,
                    parent,
                    false
                )
                CardsGroupViewHolder(binding)
            }

        }
    }

    override fun getItemCount(): Int = cardGroupList.size

    override fun onBindViewHolder(holder: CardsGroupViewHolder, position: Int) {

        when (holder.itemViewType) {
            0 -> {
                holder.itemsBindingAdd?.addNewGroupIcon?.setOnClickListener {
                    Toast.makeText(activityContext, "ADD", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                holder.itemsBinding?.context = activityContext
                holder.itemsBinding?.entity = cardGroupList[position]

                holder.itemsBinding?.viewmodel = viewModel

                viewModel.getCardsOfDeck(cardGroupList[position].Id, cardGroupList[position].deck)
                    .observe(fragment, Observer {
                        holder.itemsBinding?.deckSize = it.size
                    })


//        holder.itemsBinding?.cardsPagerRemainWords?.text = "${viewModel.getDeckSize(cardGroupList[position].deck)}"
//        holder.itemsBinding?.cardsPagerDoneWords?.text = "${viewModel.cardBoxSize}"


                holder.itemsBinding?.cardPagerExpandIcon?.setOnClickListener {
                    it as ImageView
                    Toast.makeText(activityContext, "${holder.itemViewType}", Toast.LENGTH_SHORT)
                        .show()
                    if (!holder.itemsBinding!!.cardsPagerLayoutBackground.isVisible) {
                        holder.itemsBinding!!.cardsPagerLayoutBackground.slideDown()
                        it.setImageResource(R.drawable.ic_ic_chevron_top)
                    } else {
                        holder.itemsBinding!!.cardsPagerLayoutBackground.slideUp()
                        it.setImageResource(R.drawable.ic_chevron_down)
                    }
                }

                holder.itemsBinding?.cardsPagerStart?.setOnClickListener {
                    val intent = Intent(activityContext, CardsActivity::class.java)
                    intent.putExtra("COLOR_ID", cardGroupList[position].color)
                    intent.putExtra("CARD_DECK", cardGroupList[position].deck)
                    intent.putExtra("GROUP_ID", cardGroupList[position].Id)

                    activityContext.startActivity(intent)
                }
            }
        }


    }

    fun add(items: List<CardGroupEntity>) {
        val previousDataSize: Int = this.itemCount
        if (previousDataSize == 0) {
            cardGroupList.addAll(items)
            notifyItemInserted(1)
        } else {
            cardGroupList.addAll(items)
            notifyItemRangeInserted(previousDataSize - 1, items.size - 1)
        }
    }

    fun cleanAdapter() {
        cardGroupList.clear()
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        cardGroupList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }


}