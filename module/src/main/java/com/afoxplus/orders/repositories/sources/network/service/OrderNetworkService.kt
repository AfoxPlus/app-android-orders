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
import com.afoxplus.orders.repositories.sources.network.api.response.OrderBaseResponse
import com.afoxplus.orders.repositories.sources.network.api.response.OrderResponse
import com.afoxplus.orders.repositories.sources.network.api.response.toEntity
import com.afoxplus.uikit.common.ResultState
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

internal class OrderNetworkService @Inject constructor(
    private val orderApiNetwork: OrderApiNetwork,
    private val appProperties: AppProperties
) : OrderNetworkDataSource {

    override suspend fun sendOrder(order: Order): ResultState<String> {
        return try {
            val orderRequest = OrderRequest.getOrderRequest(order)
            val headerMap = mapOf(API_HEADERS_CURRENCY_ID to appProperties.getCurrencyID())
            val result = orderApiNetwork.sendOrder(headerMap, orderRequest)
            if (result.isSuccessful) {
                if (result.body()?.success == true) {
                    ResultState.Success(
                        result.body()?.message?.value ?: "¡Pedido enviado correctamente!"
                    )
                } else ResultState.Error(orderBusinessException(result))
            } else ResultState.Error(getApiErrorException())
        } catch (exception: IOException) {
            ResultState.Error(getApiErrorException())
        }
    }

    private fun orderBusinessException(result: Response<OrderBaseResponse<OrderResponse>>): Exception {
        return result.body()?.message?.let { message ->
            OrderBusinessException(
                contentMessage = ExceptionMessage(
                    value = message.value,
                    info = message.info ?: ""
                )
            )
        } ?: getApiErrorException()
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
        return response.body()?.payload?.map {
            it.toEntity()
        } ?: emptyList()
    }

    companion object {
        const val API_HEADERS_CURRENCY_ID = "currency_id"
    }
}