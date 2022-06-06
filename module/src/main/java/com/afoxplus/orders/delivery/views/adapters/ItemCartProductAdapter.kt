package com.afoxplus.orders.delivery.views.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemCartProductListener
import com.afoxplus.orders.delivery.views.adapters.viewholders.ItemCartProductViewHolder
import com.afoxplus.orders.entities.OrderDetail

internal class ItemCartProductAdapter(private val itemCartProductListener: ItemCartProductListener) :
    ListAdapter<OrderDetail, ItemCartProductViewHolder>(ItemCartProductUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCartProductViewHolder =
        ItemCartProductViewHolder.from(parent, itemCartProductListener)

    override fun onBindViewHolder(holder: ItemCartProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ItemCartProductUtilCallback : DiffUtil.ItemCallback<OrderDetail>() {
        override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean =
            oldItem.product.code == newItem.product.code

        override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean =
            oldItem.product == newItem.product

    }
}