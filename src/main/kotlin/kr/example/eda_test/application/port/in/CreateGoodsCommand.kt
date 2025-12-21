package kr.example.eda_test.application.port.`in`

import java.math.BigDecimal

data class CreateGoodsCommand(
    val name: String,
    val price: BigDecimal,
)