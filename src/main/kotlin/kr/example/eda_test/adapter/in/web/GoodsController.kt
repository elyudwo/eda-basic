package kr.example.eda_test.adapter.`in`.web

import kr.example.eda_test.application.port.`in`.CreateGoodsCommand
import kr.example.eda_test.application.port.`in`.GoodsUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stock")
class GoodsController(
    private val goodsUseCase: GoodsUseCase
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createGoods(@RequestBody createGoodsRequest: CreateGoodsRequest) {
        val command = CreateGoodsCommand(
            name = createGoodsRequest.name,
            price = createGoodsRequest.price,
        )

        goodsUseCase.createGoods(command)

    }

}