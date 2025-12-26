package kr.example.eda_test.temporal.activity

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod
import kr.example.eda_test.application.port.`in`.CreateGoodsCommand

@ActivityInterface
interface GoodsActivities {

    @ActivityMethod
    fun validateGoods(command: CreateGoodsCommand): Boolean

    @ActivityMethod
    fun saveGoods(command: CreateGoodsCommand): String

    @ActivityMethod
    fun publishGoodsCreatedEvent(command: CreateGoodsCommand)

    @ActivityMethod
    fun initializeInventory(goodsId: String)
}
