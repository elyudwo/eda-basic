package kr.example.eda_test.application.port.out

import java.math.BigDecimal

data class GoodsCreatedEvent(
    val name: String,
    val price: BigDecimal,
)
