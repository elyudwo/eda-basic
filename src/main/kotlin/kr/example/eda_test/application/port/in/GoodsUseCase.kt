package kr.example.eda_test.application.port.`in`

import kotlinx.coroutines.future.await
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class GoodsUseCase(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    suspend fun createGoods(createGoodsCommand: CreateGoodsCommand): String {
        // Avro 생성 클래스 사용
        val event = kr.example.eda_test.event.GoodsCreatedEvent(
            createGoodsCommand.name,
            createGoodsCommand.price
        )

        kafkaTemplate.send("goods-created", event)

        return "Goods created and event published successfully"
    }
}