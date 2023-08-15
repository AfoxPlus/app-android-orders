package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.FragmentAdditionalOrderInfoBinding
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemPaymentMethodListener
import com.afoxplus.orders.delivery.views.modal.PaymentMethodModalBottomSheet
import com.afoxplus.orders.entities.Client
import com.afoxplus.orders.entities.OrderType
import com.afoxplus.uikit.bus.UIKitEventObserver
import com.afoxplus.uikit.fragments.UIKitBaseFragment
import com.afoxplus.uikit.objects.vendor.PaymentMethod
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdditionalOrderInfoFragment : UIKitBaseFragment(), ItemPaymentMethodListener {
    private lateinit var binding: FragmentAdditionalOrderInfoBinding
    private val cartProductsViewModel: ShopCartViewModel by activityViewModels()
    private val paymentMethodModalBottomSheet = PaymentMethodModalBottomSheet(this)

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentAdditionalOrderInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun getInstance(): AdditionalOrderInfoFragment = AdditionalOrderInfoFragment()
    }

    override fun observerViewModel() {
        super.observerViewModel()
        cartProductsViewModel.eventValidateTableOrder.observe(
            viewLifecycleOwner,
            UIKitEventObserver {
                cartProductsViewModel.sendOrder(getClientInfo())
            })

        cartProductsViewModel.errorClientNameLiveData.observe(viewLifecycleOwner) {
            binding.clientName.error = it
        }

        cartProductsViewModel.errorClientPhoneNumberLiveData.observe(viewLifecycleOwner) {
            binding.clientPhone.error = it
        }

        cartProductsViewModel.order.observe(viewLifecycleOwner) { order ->
            setupChipInfo(order.orderType)
        }
    }

    override fun setUpView() {
        binding.paymentMethodButton.setOnClickListener {
            displayPaymentMethods()
        }
    }

    private fun displayPaymentMethods() {
        val paymentMethods = cartProductsViewModel.fetchPaymentMethods()
        paymentMethodModalBottomSheet.show(parentFragmentManager, "PaymentMethodModalBottomSheet")
        paymentMethodModalBottomSheet.submitList(paymentMethods)
    }

    private fun getClientInfo(): Client {
        return Client(
            name = binding.clientName.text,
            phone = binding.clientPhone.text,
            addressReference = binding.clientAddressReference.text
        )
    }

    private fun setupChipInfo(orderType: OrderType) {
        when (orderType) {
            OrderType.Local -> {
                binding.chipInfoTitle.text = orderType.toString()
                binding.chipInfo.backgroundTintList =
                    resources.getColorStateList(R.color.dark_03, null)
                binding.clientAddressReference.visibility = View.GONE
                binding.clientPhone.hint =
                    getString(R.string.orders_table_client_cellphone)
            }

            OrderType.Delivery -> {
                binding.chipInfoTitle.text = orderType.toString()
                binding.chipInfo.backgroundTintList =
                    resources.getColorStateList(R.color.red_01, null)
                binding.clientPhone.hint =
                    " ${getString(R.string.orders_table_client_cellphone)}*"
                binding.clientAddressReference.visibility = View.VISIBLE
            }
        }
    }

    override fun onSelected(paymentMethod: PaymentMethod) {
        println("Here is payment: $paymentMethod")
    }

}