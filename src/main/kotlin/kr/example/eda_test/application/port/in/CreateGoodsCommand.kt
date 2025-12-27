package kr.example.eda_test.application.port.`in`

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class CreateGoodsCommand @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("price") val price: BigDecimal,
)