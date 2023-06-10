package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.FragmentOrdersChoseTableBinding
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.entities.DeliveryType
import com.afoxplus.uikit.bus.UIKitEventObserver
import com.afoxplus.uikit.fragments.UIKitBaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TableOrderFragment : UIKitBaseFragment() {
    private lateinit var binding: FragmentOrdersChoseTableBinding
    private val cartProductsViewModel: ShopCartViewModel by activityViewModels()

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrdersChoseTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun getInstance(): TableOrderFragment = TableOrderFragment()
    }

    override fun observerViewModel() {
        super.observerViewModel()
        cartProductsViewModel.eventValidateTableOrder.observe(
            viewLifecycleOwner,
            UIKitEventObserver {
                cartProductsViewModel.sendOrder(
                    binding.tableClientName.text,
                    binding.tableClientPhone.text
                )
            })

        cartProductsViewModel.errorClientNameLiveData.observe(viewLifecycleOwner) {
            binding.tableClientName.error = it
        }

        cartProductsViewModel.errorClientPhoneNumberLiveData.observe(viewLifecycleOwner) {
            binding.tableClientPhone.error = it
        }

        cartProductsViewModel.order.observe(viewLifecycleOwner) { order ->
            setupChipInfo(order.deliveryType)
        }
    }

    private fun setupChipInfo(deliveryType: DeliveryType) {
        when (deliveryType) {
            DeliveryType.Local -> {
                binding.chipInfoTitle.text = deliveryType.value
                binding.chipInfo.backgroundTintList =
                    resources.getColorStateList(R.color.dark_03, null)
            }

            DeliveryType.Delivery -> {
                binding.chipInfoTitle.text = deliveryType.value
                binding.chipInfo.backgroundTintList =
                    resources.getColorStateList(R.color.red_01, null)
            }
        }
    }

}