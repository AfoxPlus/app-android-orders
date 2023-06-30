package com.afoxplus.orders.delivery.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ItemOrderStatusProductBinding

import com.afoxplus.orders.entities.DetailStatus

class ItemDetailStatusProductAdapter :
    ListAdapter<DetailStatus, ItemDetailStatusProductAdapter.ItemDetailStatusProductViewHolder>(
        DetailStatusProductDiffUtil()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemDetailStatusProductViewHolder {
        return ItemDetailStatusProductViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemDetailStatusProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: MutableList<DetailStatus>?) {
        super.submitList(list)
    }

    class ItemDetailStatusProductViewHolder(
        private val binding: ItemOrderStatusProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ItemDetailStatusProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemOrderStatusProductBinding.inflate(layoutInflater, parent, false)
                return ItemDetailStatusProductViewHolder(binding)
            }
        }

        fun bind(item: DetailStatus) {
            with(binding) {
                tvProductTitle.text = item.title
                tvProductDescription.text = item.description
                tvProductPrice.text = item.unitPrice
                tvProductQuantity.text = String.format(
                    itemView.context.getString(R.string.orders_status_product_quantity),
                    item.quantity
                )
                tvProductSubTotal.text = item.subTotal
            }
        }
    }
}

class DetailStatusProductDiffUtil : DiffUtil.ItemCallback<DetailStatus>() {

    override fun areItemsTheSame(oldItem: DetailStatus, newItem: DetailStatus): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: DetailStatus, newItem: DetailStatus): Boolean {
        return oldItem == newItem
    }

}