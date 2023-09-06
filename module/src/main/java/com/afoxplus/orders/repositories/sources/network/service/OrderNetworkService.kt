package com.afoxplus.orders.repositories.sources.network.service

import com.afoxplus.network.api.NetworkResult
import com.afoxplus.network.global.AppProperties
import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.entities.OrderStatus
import com.afoxplus.orders.repositories.sources.network.OrderNetworkDataSource
import com.afoxplus.orders.repositories.sources.network.api.OrderApiNetwork
import com.afoxplus.orders.repositories.sources.network.api.request.OrderRequest
import com.afoxplus.orders.repositories.sources.network.api.response.toEntity
import com.afoxplus.uikit.result.ErrorMessage
import com.afoxplus.uikit.result.ErrorType
import com.afoxplus.uikit.result.ResultState
import javax.inject.Inject

internal class OrderNetworkService @Inject constructor(
    private val orderApiNetwork: OrderApiNetwork,
    private val appProperties: AppProperties
) : OrderNetworkDataSource {

    override suspend fun sendOrder(order: Order): ResultState<String> {
        val orderRequest = OrderRequest.getOrderRequest(order)
        val headerMap = mapOf(API_HEADERS_CURRENCY_ID to appProperties.getCurrencyID())
        return when (val response = orderApiNetwork.sendOrder(headerMap, orderRequest)) {
            is NetworkResult.Success -> {
                ResultState.Success("¡Pedido enviado correctamente!")
            }

            is NetworkResult.Error -> {
                ResultState.Error(
                    ErrorMessage(
                        code = response.code,
                        title = "",
                        message = response.message ?: API_ORDER_ERROR_MESSAGE,
                        errorType = ErrorType.ERROR
                    )
                )
            }

            is NetworkResult.Exception -> {
                ResultState.Error(
                    ErrorMessage(
                        title = "",
                        message = API_ORDER_ERROR_MESSAGE,
                        errorType = ErrorType.EXCEPTION
                    )
                )
            }
        }
    }

    override suspend fun getOrderStatus(): ResultState<List<OrderStatus>> {
        return when (val response = orderApiNetwork.getOrderStatus()) {
            is NetworkResult.Success -> {
                ResultState.Success(data = response.data.payload.map { it.toEntity() })
            }

            is NetworkResult.Error -> {
                ResultState.Error(
                    ErrorMessage(
                        code = response.code,
                        title = "",
                        message = response.message ?: API_ORDER_ERROR_MESSAGE,
                        errorType = ErrorType.ERROR
                    )
                )
            }

            is NetworkResult.Exception -> {
                ResultState.Error(
                    ErrorMessage(
                        title = "",
                        message = API_ORDER_ERROR_MESSAGE,
                        errorType = ErrorType.EXCEPTION
                    )
                )
            }
        }
    }

    companion object {
        private const val API_ORDER_ERROR_MESSAGE = "API Orders Internal Error"
        const val API_HEADERS_CURRENCY_ID = "currency_id"
    }
}