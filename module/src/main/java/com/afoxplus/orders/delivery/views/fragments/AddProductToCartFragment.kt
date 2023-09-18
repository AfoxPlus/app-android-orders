package com.afoxplus.orders.delivery.views.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.afoxplus.orders.R
import com.afoxplus.orders.databinding.FragmentOrdersAddProductToCartBinding
import com.afoxplus.orders.delivery.viewmodels.AddProductToOrderViewModel
import com.afoxplus.orders.delivery.views.adapters.ItemAppetizerAdapter
import com.afoxplus.orders.delivery.views.adapters.listeners.ItemAppetizerListener
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.fragments.UIKitBaseFragment
import com.afoxplus.uikit.modal.UIKitModal

class AddProductToCartFragment : UIKitBaseFragment(), ItemAppetizerListener {

    private lateinit var binding: FragmentOrdersAddProductToCartBinding
    private val addProductToOrderViewModel: AddProductToOrderViewModel by activityViewModels()

    private val adapter: ItemAppetizerAdapter by lazy { ItemAppetizerAdapter(this) }

    companion object {
        fun getInstance(): AddProductToCartFragment = AddProductToCartFragment()
    }

    override fun getMainView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = FragmentOrdersAddProductToCartBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun setUpView() {
        binding.quantityButtonQuantity.onValueChangeListener = { quantity ->
            addProductToOrderViewModel.calculateSubTotalByProduct(quantity)
        }
        binding.recyclerProductAppetizer.adapter = adapter
        binding.quantityButtonQuantity.deleteIcon = R.drawable.ic_minus
    }

    override fun observerViewModel() {
        addProductToOrderViewModel.quantity.observe(viewLifecycleOwner) {
            binding.quantityButtonQuantity.value = it ?: 0
        }

        addProductToOrderViewModel.appetizerVisibility.observe(viewLifecycleOwner) {
            binding.sectionAppetizer.visibility = it
        }

        addProductToOrderViewModel.appetizersStateModel.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        addProductToOrderViewModel.appetizersShowModal.observe(viewLifecycleOwner) {
            displayAppetizerAlertModal()
        }
    }

    private fun displayAppetizerAlertModal() {
        UIKitModal.Builder(parentFragmentManager)
            .title(getString(R.string.order_appetizers_modal_title))
            .message(getString(R.string.order_appetizers_modal_message))
            .positiveButton(getString(R.string.order_appetizers_modal_action)) {
                it.dismiss()
            }
            .show()
    }

    override fun updateQuantity(product: Product, quantity: Int) {
        addProductToOrderViewModel.handleAppetizerQuantity(product, quantity)
    }
}