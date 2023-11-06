package com.afoxplus.orders.data.sources.network.service

import com.afoxplus.network.global.AppProperties
import com.afoxplus.orders.cross.exceptions.ApiErrorException
import com.afoxplus.orders.cross.exceptions.ExceptionMessage
import com.afoxplus.orders.cross.exceptions.OrderBusinessException
import com.afoxplus.orders.data.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.data.sources.network.api.OrderApiNetwork
import com.afoxplus.orders.data.sources.network.api.request.OrderRequest
import com.afoxplus.orders.data.sources.network.api.response.OrderMessageResponse
import com.afoxplus.orders.data.sources.network.api.response.toEntity
import com.afoxplus.orders.domain.entities.Order
import com.afoxplus.orders.domain.entities.OrderStatus
import com.afoxplus.uikit.common.ResultState
import javax.inject.Inject

internal class OrderNetworkService @Inject constructor(
    private val orderApiNetwork: OrderApiNetwork,
    private val appProperties: AppProperties
) : OrderNetworkDataSource {

    override suspend fun sendOrder(order: Order): ResultState<String> {
        val orderRequest = OrderRequest.getOrderRequest(order)
        val headerMap = mapOf(API_HEADERS_CURRENCY_ID to appProperties.getCurrencyID())
        val result = orderApiNetwork.sendOrder(headerMap, orderRequest)
        return if (result.isSuccessful) {
            val body = result.body() ?: return ResultState.Error(getApiErrorException())
            if (body.success) {
                ResultState.Success(body.message.value)
            } else ResultState.Error(orderBusinessException(body.message))
        } else ResultState.Error(getApiErrorException())
    }

    private fun orderBusinessException(message: OrderMessageResponse): Exception {
        return OrderBusinessException(
            contentMessage = ExceptionMessage(
                value = message.value,
                info = message.info ?: ""
            )
        )
    }

    private fun getApiErrorException(): ApiErrorException {
        return ApiErrorException(
            contentMessage = ExceptionMessage(
                value = "No se ha podido enviar el pedido",
                info = "¿Quieres intentarlo nuevamente?"
            )
        )
    }

    override suspend fun getOrderStatus(): List<OrderStatus> {
        val response = orderApiNetwork.getOrderStatus()
        val body = response.body() ?: return emptyList()
        return body.payload.map {
            it.toEntity()
        }
    }

    companion object {
        const val API_HEADERS_CURRENCY_ID = "currency_id"
    }
}