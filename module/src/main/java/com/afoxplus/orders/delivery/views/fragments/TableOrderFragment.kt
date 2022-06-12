package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.FragmentOrdersChoseTableBinding
import com.afoxplus.orders.delivery.viewmodels.ShopCartViewModel
import com.afoxplus.uikit.fragments.BaseFragment
import com.afoxplus.uikit.objects.vendor.VendorAction
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TableOrderFragment : BaseFragment() {
    private lateinit var binding: FragmentOrdersChoseTableBinding
    private val cartProductsViewModel: ShopCartViewModel by activityViewModels()

    @Inject
    lateinit var vendorAction: VendorAction

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrdersChoseTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun getInstance(): TableOrderFragment = TableOrderFragment()
    }

    override fun observerViewModel() {
        super.observerViewModel()
        vendorAction.fetch()?.run {
            binding.tableNumber.text = getString(R.string.orders_table_number, tableId)
        }
    }

}