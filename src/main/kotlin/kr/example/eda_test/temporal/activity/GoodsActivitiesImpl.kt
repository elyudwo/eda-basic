package kr.example.eda_test.temporal.activity

import kr.example.eda_test.application.port.`in`.CreateGoodsCommand
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*

@Component
class GoodsActivitiesImpl(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) : GoodsActivities {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun validateGoods(command: CreateGoodsCommand): Boolean {
        logger.info("Validating goods: ${command.name}")

        // 유효성 검증 로직
        if (command.name.isBlank()) {
            logger.warn("Goods name is blank")
            return false
        }

        if (command.price <= BigDecimal.ZERO) {
            logger.warn("Goods price is invalid: ${command.price}")
            return false
        }

        logger.info("Goods validation passed for: ${command.name}")
        return true
    }

    override fun saveGoods(command: CreateGoodsCommand): String {
        logger.info("Saving goods: ${command.name}")

        // 실제로는 DB에 저장하는 로직
        val goodsId = UUID.randomUUID().toString()

        logger.info("Goods saved with ID: $goodsId")
        return goodsId
    }

    override fun publishGoodsCreatedEvent(command: CreateGoodsCommand) {
        logger.info("Publishing goods created event for: ${command.name}")

        // Avro 이벤트 생성 및 발행
        val event = kr.example.eda_test.event.GoodsCreatedEvent(
            command.name,
            command.price
        )

        kafkaTemplate.send("goods-created", event)
        logger.info("Goods created event published successfully")
    }

    override fun initializeInventory(goodsId: String) {
        logger.info("Initializing inventory for goods ID: $goodsId")

        // 재고 초기화 로직 (시뮬레이션)
        // 실제로는 재고 서비스 호출 또는 DB 업데이트

        logger.info("Inventory initialized for goods ID: $goodsId")
    }
}
