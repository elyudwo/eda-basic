package kr.example.eda_test.temporal.workflow

import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Workflow
import kr.example.eda_test.application.port.`in`.CreateGoodsCommand
import kr.example.eda_test.temporal.activity.GoodsActivities
import java.time.Duration

class GoodsWorkflowImpl : GoodsWorkflow {

    private val activities: GoodsActivities = Workflow.newActivityStub(
        GoodsActivities::class.java,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(30))
            .build()
    )

    override fun createGoods(command: CreateGoodsCommand): String {
        // Step 1: 상품 유효성 검증
        val validationResult = activities.validateGoods(command)
        if (!validationResult) {
            return "Validation failed for goods: ${command.name}"
        }

        // Step 2: 상품 데이터 저장 (시뮬레이션)
        val goodsId = activities.saveGoods(command)

        // Step 3: Kafka 이벤트 발행
        activities.publishGoodsCreatedEvent(command)

        // Step 4: 재고 초기화 (시뮬레이션)
        activities.initializeInventory(goodsId)

        return "Goods created successfully with ID: $goodsId"
    }
}
