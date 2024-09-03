package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.databinding.FragmentOrdersShopCartBinding
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.adapters.ItemCartProductAdapter
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemCartProductListener
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.uikit.fragments.UIKitBaseFragment

class ShopCartFragment : UIKitBaseFragment(), ItemCartProductListener {
    private lateinit var binding: FragmentOrdersShopCartBinding

    private val cartProductsViewModel: ShopCartViewModel by activityViewModels()
    private val adapter: ItemCartProductAdapter by lazy { ItemCartProductAdapter(this) }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrdersShopCartBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        fun getInstance(): ShopCartFragment = ShopCartFragment()
    }

    override fun setUpView() {
        binding.recyclerProductsOrder.adapter = adapter
    }

    override fun observerViewModel() {
        cartProductsViewModel.order.observe(viewLifecycleOwner) { order ->
            val newListO: MutableList<OrderDetail> = mutableListOf()
            order.getOrderDetails().map {
                newListO.add(
                    OrderDetail(
                        product = it.product,
                        quantity = it.quantity,
                        notes = it.notes,
                        saleOrderItemStrategy = it.saleOrderItemStrategy
                    )
                )
            }
            adapter.submitList(newListO)
            displayOrder(order)
        }
    }

    private fun displayOrder(order: Order) {
        binding.subTotalOrder.text = order.getTotalWithFormat()
        binding.shopOrderSummaryQuantityValue.text = order.getTotalQuantity().toString()
    }

    override fun deleteItem(orderDetail: OrderDetail) {
        cartProductsViewModel.deleteItem(orderDetail)
    }

    override fun updateQuantity(orderDetail: OrderDetail, quantity: Int) {
        cartProductsViewModel.updateQuantity(orderDetail, quantity)
    }

    override fun editProduct(orderDetail: OrderDetail) {
        cartProductsViewModel.editMenuDish(orderDetail)
    }
}