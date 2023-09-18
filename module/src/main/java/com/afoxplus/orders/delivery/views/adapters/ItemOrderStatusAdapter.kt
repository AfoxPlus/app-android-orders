package com.afoxplus.orders.delivery.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ItemOrderStatusBinding
import com.afoxplus.orders.domain.entities.OrderStatus

class ItemOrderStatusAdapter(
    private val onClick: (OrderStatus) -> Unit
) : ListAdapter<OrderStatus, ItemOrderStatusAdapter.ItemOrderStatusViewHolder>(OrderStatusDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemOrderStatusViewHolder {
        return ItemOrderStatusViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: ItemOrderStatusViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<OrderStatus>?) {
        super.submitList(list)
    }

    class ItemOrderStatusViewHolder(
        private val binding: ItemOrderStatusBinding,
        private val onClick: (OrderStatus) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup, onClick: (OrderStatus) -> Unit): ItemOrderStatusViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOrderStatusBinding.inflate(layoutInflater, parent, false)
                return ItemOrderStatusViewHolder(binding, onClick)
            }
        }

        fun bind(item: OrderStatus) {
            with(binding) {
                clItemContainerOrderStatus.setOnClickListener { onClick(item) }
                tvStatusOrderRestaurantName.text = item.restaurant
                ctosAmount.description = item.total
                ctosRestaurant.configType(item.orderType.code)
                ctosRestaurant.title = item.orderType.title
                ctosRestaurant.description = item.orderType.description
                mbStatus.text = item.state
                tvStatusId.text = String.format(
                    itemView.context.getString(R.string.orders_status_id), item.number
                )
            }
        }
    }
}

class OrderStatusDiffUtil : DiffUtil.ItemCallback<OrderStatus>() {

    override fun areItemsTheSame(oldItem: OrderStatus, newItem: OrderStatus): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OrderStatus, newItem: OrderStatus): Boolean {
        return oldItem == newItem
    }

}