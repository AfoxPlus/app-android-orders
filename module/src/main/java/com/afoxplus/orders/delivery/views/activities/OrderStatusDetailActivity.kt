package com.afoxplus.orders.delivery.views.activities

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.DefaultItemAnimator
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.ActivityOrderStatusDetailBinding
import com.afoxplus.orders.delivery.views.adapters.ItemDetailStatusProductAdapter
import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.uikit.activities.UIKitBaseActivity

import com.afoxplus.uikit.extensions.parcelable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderStatusDetailActivity : UIKitBaseActivity() {

    private lateinit var binding: ActivityOrderStatusDetailBinding
    private lateinit var orderStatus: OrderStatus
    private val adapter: ItemDetailStatusProductAdapter by lazy { ItemDetailStatusProductAdapter() }

    companion object {
        fun createIntent(context: Context, orderStatus: OrderStatus): Intent {
            val intent = Intent(context, OrderStatusDetailActivity::class.java)
            intent.putExtra(OrderStatus::class.java.name, orderStatus)
            return intent
        }
    }

    override fun setMainView() {
        binding = ActivityOrderStatusDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    override fun setUpView() {
        setupAdapter()
        with(binding) {
            appBar.subtitle = orderStatus.restaurant
            tvStatusId.text = String.format(
                getString(R.string.orders_status_id),
                orderStatus.id.take(6)
            )
            tvStatusDate.text = orderStatus.date
            tvStatusAmount.text = orderStatus.total
            tvStatusClient.text = orderStatus.client.client
            tvStatusPhone.text = orderStatus.client.cel
            tvStatusReference.text = orderStatus.client.addressReference
            ctosTypeStatusOrder.configType(orderStatus.orderType.code)
            ctosTypeStatusOrder.title = orderStatus.orderType.title
            ctosTypeStatusOrder.description = orderStatus.orderType.description
            tvStatus.text = orderStatus.state
            appBar.setNavigationOnClickListener { finish() }
        }
    }

    private fun setupAdapter() {
        with(binding) {
            rvProducts.adapter = adapter
            rvProducts.setHasFixedSize(true)
            rvProducts.itemAnimator = DefaultItemAnimator()
            adapter.submitList(orderStatus.detail.toMutableList())
        }
    }

    private fun getIntentData() {
        intent.parcelable<OrderStatus>(OrderStatus::class.java.name)?.let {
            this.orderStatus = it
        }
    }

}