package com.afoxplus.orders.data.sources.local.cache

import com.afoxplus.orders.domain.entities.OrderType
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderAppetizerDetail
import com.afoxplus.orders.domain.entities.OrderDetail
import com.afoxplus.orders.data.sources.local.OrderLocalDataSource
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.objects.vendor.Vendor
import com.afoxplus.uikit.objects.vendor.VendorShared
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OrderLocalCache @Inject constructor(
    private val vendorShared: VendorShared
) : OrderLocalDataSource {

    private var order: Order? = null
    private val orderStateFlow: MutableSharedFlow<Order?> by lazy {
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }

    override suspend fun addOrUpdateProductToCurrentOrder(quantity: Int, product: Product) {
        val order = order ?: newOrder()
        order.addUpdateOrDeleteProductWithQuantity(product, quantity)
        orderStateFlow.emit(order)
    }

    override fun clearCurrentOrder() {
        order = null
    }

    override fun findProductInOrder(product: Product): OrderDetail? {
        return order?.run {
            getOrderDetails().find { item ->
                item.product.code == product.code
            }
        }
    }

    override suspend fun getCurrentOrder(): SharedFlow<Order?> {
        orderStateFlow.emit(order)
        return orderStateFlow
    }

    override suspend fun deleteProductToCurrentOrder(product: Product) {
        order?.removeItemOrderDetailByProduct(product)
        orderStateFlow.emit(order)
    }

    override suspend fun addOrUpdateAppetizerToCurrentOrder(
        quantity: Int,
        appetizer: Product,
        product: Product
    ) {
        val orderDetail = findProductInOrder(product)
        orderDetail?.addAppetizerWithQuantity(appetizer, quantity)
    }

    override suspend fun fetchAppetizersByProduct(product: Product): List<OrderAppetizerDetail> {
        val orderDetail = findProductInOrder(product) ?: return arrayListOf()
        return orderDetail.appetizers.toList()
    }

    override suspend fun clearAppetizersByProduct(product: Product) {
        findProductInOrder(product)?.apply {
            appetizers.clear()
        }
    }

    private fun newOrder(): Order {
        val newOrder = Order(
            restaurantId = fetchVendorData().restaurantId,
            orderType = getOrderTypeOrigin()
        )
        order = newOrder
        return newOrder
    }

    private fun getOrderTypeOrigin(): OrderType {
        fetchVendorData().let {
            val orderType = it.additionalInfo["restaurant_order_type"]
            return when (orderType) {
                "DELIVERY" -> OrderType.Delivery
                else -> OrderType.Local.apply { description = it.tableId }
            }
        }
    }

    private fun fetchVendorData(): Vendor {
        return vendorShared.fetch() ?: throw Exception("No found DeliveryType")
    }
}