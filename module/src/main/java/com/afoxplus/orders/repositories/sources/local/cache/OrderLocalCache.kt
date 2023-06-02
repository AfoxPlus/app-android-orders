package com.afoxplus.orders.repositories.sources.local.cache

import com.afoxplus.orders.entities.OrderType
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderDetail
import com.afoxplus.orders.repositories.sources.local.OrderLocalDataSource
import com.afoxplus.products.entities.Product
import com.afoxplus.uikit.objects.vendor.VendorShared
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.util.*
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
        order?.addProductWithQuantity(product, quantity)
            ?: newOrder().addProductWithQuantity(product, quantity)
        orderStateFlow.emit(order)
    }

    override fun clearCurrentOrder() {
        order = null
    }

    override fun findProductInOrder(product: Product): OrderDetail? {
        return order?.getOrderDetails()?.find { item -> item.product.code == product.code }
    }

    override suspend fun getCurrentOrder(): SharedFlow<Order?> {
        orderStateFlow.emit(order)
        return orderStateFlow
    }

    override suspend fun deleteProductToCurrentOrder(product: Product) {
        order?.removeItemOrderDetailByProduct(product)
        orderStateFlow.emit(order)
    }

    private fun newOrder(): Order {
        val newOrder = Order(
            date = Calendar.getInstance().time,
            restaurantId = vendorShared.fetch()?.restaurantId ?: "",
            orderType = getDeliveryType()
        )
        order = newOrder
        return newOrder
    }

    private fun getDeliveryType(): OrderType {
        vendorShared.fetch()?.let {
            val isOwnDelivery = it.additionalInfo["restaurant_own_delivery"] == true
            return if (isOwnDelivery)
                OrderType.Delivery
            else OrderType.Local.apply { description = it.tableId }
        } ?: throw Exception("No found DeliveryType")
    }
}