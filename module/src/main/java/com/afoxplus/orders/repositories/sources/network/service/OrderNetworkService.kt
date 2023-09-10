package com.afoxplus.orders.repositories.sources.network.service

import com.afoxplus.network.global.AppProperties
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.orders.repositories.exceptions.ApiErrorException
import com.afoxplus.orders.repositories.exceptions.ExceptionMessage
import com.afoxplus.orders.repositories.exceptions.OrderBusinessException
import com.afoxplus.orders.repositories.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.repositories.sources.network.api.OrderApiNetwork
import com.afoxplus.orders.repositories.sources.network.api.request.OrderRequest
import com.afoxplus.orders.repositories.sources.network.api.response.toEntity
import com.afoxplus.uikit.common.ResultState
import java.io.IOException
import javax.inject.Inject

internal class OrderNetworkService @Inject constructor(
    private val orderApiNetwork: OrderApiNetwork,
    private val appProperties: AppProperties
) : OrderNetworkDataSource {

    override suspend fun sendOrder(order: Order): ResultState<String> {
        try {
            val orderRequest = OrderRequest.getOrderRequest(order)
            val headerMap = mapOf(API_HEADERS_CURRENCY_ID to appProperties.getCurrencyID())
            val result = orderApiNetwork.sendOrder(headerMap, orderRequest)
            if (result.isSuccessful) {
                return if (result.body()?.success == true) {
                    ResultState.Success(
                        result.body()?.message?.value ?: "¡Pedido enviado correctamente!"
                    )
                } else {
                    ResultState.Error(
                        OrderBusinessException(
                            contentMessage = ExceptionMessage(
                                value = result.body()?.message?.value
                                    ?: "Hubo un problema al enviar el pedido",
                                info = result.body()?.message?.info
                                    ?: "Ha ocurrido un problema al enviar el pedido, intentalo nuevamente"
                            )
                        )
                    )
                }
            } else return handleError()
        } catch (exception: IOException) {
            return handleError()
        }
    }

    private fun handleError(): ResultState<String> {
        return ResultState.Error(
            ApiErrorException(
                contentMessage = ExceptionMessage(
                    value = "No se ha podido enviar el pedido",
                    info = "¿Quieres intentarlo nuevamente?"
                )
            )
        )
    }

    override suspend fun getOrderStatus(): List<OrderStatus> {
        val response = orderApiNetwork.getOrderStatus()
        return response.body()?.payload?.map {
            it.toEntity()
        } ?: emptyList()
    }

    companion object {
        const val API_HEADERS_CURRENCY_ID = "currency_id"
    }
}