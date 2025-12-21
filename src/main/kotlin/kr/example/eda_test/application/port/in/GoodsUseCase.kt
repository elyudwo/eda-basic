package kr.example.eda_test.application.port.`in`

import kotlinx.coroutines.future.await
import kr.example.eda_test.application.port.out.GoodsCreatedEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class GoodsUseCase(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    suspend fun createGoods(createGoodsCommand: CreateGoodsCommand): String {
        val event = GoodsCreatedEvent(
            name = createGoodsCommand.name,
            price = createGoodsCommand.price
        )

        kafkaTemplate.send("goods-created", event)

        return "Goods created and event published successfully"
    }
}