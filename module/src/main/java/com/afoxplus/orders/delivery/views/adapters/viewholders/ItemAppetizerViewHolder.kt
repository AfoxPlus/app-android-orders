package com.afoxplus.orders.delivery.views.adapters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afoxplus.orders.databinding.ItemOrdersCartAppetizerBinding
import com.afoxplus.orders.delivery.models.AppetizerStateModel
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemAppetizerListener
import com.afoxplus.orders.entities.OrderAppetizerDetail
import com.afoxplus.products.entities.Product

internal class ItemAppetizerViewHolder(
    private val binding: ItemOrdersCartAppetizerBinding,
    private val itemAppetizerListener: ItemAppetizerListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(appetizerStateModel: AppetizerStateModel) {
        binding.appetizerName.text = appetizerStateModel.orderAppetizerDetail.product.name
        binding.appetizerQuantity.value = appetizerStateModel.orderAppetizerDetail.quantity
        binding.appetizerQuantity.isEnable = appetizerStateModel.isEnable
        binding.appetizerQuantity.onValueChangeListener = { quantity ->
            itemAppetizerListener.updateQuantity(appetizerStateModel.orderAppetizerDetail.product, quantity)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            itemAppetizerListener: ItemAppetizerListener
        ): ItemAppetizerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOrdersCartAppetizerBinding.inflate(layoutInflater, parent, false)
            return ItemAppetizerViewHolder(binding, itemAppetizerListener)
        }
    }
}