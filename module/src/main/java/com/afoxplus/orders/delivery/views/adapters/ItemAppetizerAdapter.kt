package com.afoxplus.orders.delivery.views.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.afoxplus.orders.delivery.models.AppetizerStateModel
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemAppetizerListener
import com.afoxplus.orders.delivery.views.adapters.viewholders.ItemAppetizerViewHolder
import com.afoxplus.products.entities.Product

internal class ItemAppetizerAdapter(private val itemAppetizerListener: ItemAppetizerListener) :
    ListAdapter<AppetizerStateModel, ItemAppetizerViewHolder>(ItemAppetizerUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAppetizerViewHolder =
        ItemAppetizerViewHolder.from(parent, itemAppetizerListener)

    override fun onBindViewHolder(holder: ItemAppetizerViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ItemAppetizerUtilCallback : DiffUtil.ItemCallback<AppetizerStateModel>() {
        override fun areItemsTheSame(oldItem: AppetizerStateModel, newItem: AppetizerStateModel): Boolean =
            oldItem.orderAppetizerDetail.product.code == newItem.orderAppetizerDetail.product.code

        override fun areContentsTheSame(oldItem: AppetizerStateModel, newItem: AppetizerStateModel): Boolean =
            oldItem == newItem
    }
}