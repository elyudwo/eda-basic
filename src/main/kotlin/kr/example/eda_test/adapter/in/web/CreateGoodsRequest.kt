package kr.example.eda_test.adapter.`in`.web

import java.math.BigDecimal

data class CreateGoodsRequest(
    val name: String,
    val price: BigDecimal,
)