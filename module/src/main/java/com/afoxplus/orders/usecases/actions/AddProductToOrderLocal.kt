package com.afoxplus.orders.usecases.actions

import com.afoxplus.orders.entities.Order
import com.afoxplus.orders.usecases.repositories.OrderRepository
import com.afoxplus.products.entities.Product
import com.afoxplus.products.usecases.actions.FindSaleProductStrategy
import com.afoxplus.products.usecases.actions.HasProductStock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

interface AddProductToOrderLocal {
    suspend operator fun invoke(product: Product):Flow<Result<Order>>
}

internal class AddProductToOrderLocalUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val hasProductStock: HasProductStock,
    private val findSaleProductStrategy: FindSaleProductStrategy,
    private val findSaleOrderItemStrategy: FindSaleOrderItemStrategy
) : AddProductToOrderLocal {
    override suspend fun invoke(product: Product): Flow<Result<Order>> = flow{
        try {
            if (hasProductStock(product,0)) {
                //val productStrategy = findSaleProductStrategy(product)
                //product.addSaleProductStrategy(productStrategy)
                //val orderDetailStrategy = findSaleOrderItemStrategy(product)
                //val order = orderRepository.addProduct(product, quantity)
                //order.addProduct(product, quantity, orderDetailStrategy)
                //emit(Result.success(order))
            } else
                emit(Result.failure<Order>(IOException("Has not stock product")))
        } catch (ex: Throwable) {
            emit(Result.failure<Order>(ex))
        }
    }

}
