package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.CompositePageTransformer
import com.afoxplus.orders.databinding.FragmentOrderStatusBinding
import com.afoxplus.orders.delivery.models.OrderStateUIModel
import com.afoxplus.orders.delivery.viewmodels.OrderStatusViewModel
import com.afoxplus.orders.delivery.views.activities.OrderStatusDetailActivity
import com.afoxplus.orders.delivery.views.adapters.ItemOrderStatusAdapter
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemClickListener
import com.afoxplus.orders.domain.entities.OrderStatus
import com.afoxplus.uikit.fragments.UIKitBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class OrderStatusFragment : UIKitBaseFragment(), ItemClickListener<OrderStatus> {

    private lateinit var binding: FragmentOrderStatusBinding
    private val ordersViewModel: OrderStatusViewModel by activityViewModels()
    private val adapter: ItemOrderStatusAdapter by lazy { ItemOrderStatusAdapter { onClick(it) } }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrderStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setUpView() {
        setupAdapter()
    }

    override fun observerViewModel() {
        ordersViewModel.orders.observe(viewLifecycleOwner) { state ->
            when (state) {
                is OrderStateUIModel.Error -> {
                    binding.rvOrderStatus.visibility = View.GONE
                }
                is OrderStateUIModel.Success -> {
                    binding.rvOrderStatus.visibility = View.VISIBLE
                    adapter.submitList(state.orders.toMutableList())
                }
                else -> {
                    //Nothing
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ordersViewModel.fetchStateOrders()
    }

    private fun setupAdapter() {
        with(binding) {
            rvOrderStatus.adapter = adapter
            rvOrderStatus.offscreenPageLimit = 3
            val cpt = CompositePageTransformer()
            cpt.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = (0.90f + r * 0.04f)
            }
            rvOrderStatus.setPageTransformer(cpt)
        }
    }

    override fun onClick(item: OrderStatus) {
        startActivity(OrderStatusDetailActivity.createIntent(requireContext(), item))
    }

}