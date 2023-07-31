package com.afoxplus.orders.delivery.views.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ItemOrdersCartProductBinding
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemCartProductListener
import com.afoxplus.orders.entities.OrderDetail

internal class ItemCartProductViewHolder private constructor(
    private val binding: ItemOrdersCartProductBinding,
    private val itemCartProductListener: ItemCartProductListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(orderDetail: OrderDetail) {
        binding.productName.text = orderDetail.product.name
        binding.productPrice.text = orderDetail.product.getPriceForSaleWithFormat()
        binding.productQuantity.value = orderDetail.quantity
        binding.productQuantity.onValueChangeListener = { quantity ->
            itemCartProductListener.updateQuantity(orderDetail, quantity)
        }
        binding.productQuantity.onDeleteActionListener = {
            itemCartProductListener.deleteItem(orderDetail)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            itemCartProductListener: ItemCartProductListener
        ): ItemCartProductViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOrdersCartProductBinding.inflate(layoutInflater, parent, false)
            return ItemCartProductViewHolder(binding, itemCartProductListener)
        }
    }

}