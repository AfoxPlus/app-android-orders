package com.afoxplus.orders.delivery.views.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afoxplus.orders.databinding.ItemOrdersCartProductBinding
import com.afoxplus.orders.entities.OrderDetail

internal class ItemCartProductViewHolder private constructor(
    private val binding: ItemOrdersCartProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(orderDetail: OrderDetail) {
        binding.productName.text = orderDetail.product.name
        binding.productPrice.text = orderDetail.product.getPriceForSaleWithFormat()
        binding.productQuantity.value = orderDetail.quantity
    }

    companion object {
        fun from(parent: ViewGroup): ItemCartProductViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOrdersCartProductBinding.inflate(layoutInflater, parent, false)
            return ItemCartProductViewHolder(binding)
        }
    }

}