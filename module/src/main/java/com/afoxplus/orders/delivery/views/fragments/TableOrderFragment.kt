package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.FragmentOrdersChoseTableBinding
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.uikit.bus.UIKitEventObserver
import com.afoxplus.uikit.fragments.UIKitBaseFragment
import com.afoxplus.uikit.objects.vendor.VendorShared
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TableOrderFragment : UIKitBaseFragment() {
    private lateinit var binding: FragmentOrdersChoseTableBinding
    private val cartProductsViewModel: ShopCartViewModel by activityViewModels()

    @Inject
    lateinit var vendorShared: VendorShared

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrdersChoseTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun getInstance(): TableOrderFragment = TableOrderFragment()
    }

    override fun observerViewModel() {
        super.observerViewModel()
        cartProductsViewModel.eventValidateTableOrder.observe(viewLifecycleOwner, UIKitEventObserver {
            cartProductsViewModel.sendOrder(
                binding.tableNumber.text.toString(),
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
    }

    override fun setUpView() {
        super.setUpView()
        vendorShared.fetch()?.run {
            binding.tableNumber.text = getString(R.string.orders_table_number, tableId)
        }
    }

}