package kr.example.eda_test.adapter.`in`.web

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import kr.example.eda_test.adapter.out.CreateGoodsResponse
import kr.example.eda_test.adapter.out.GetWorkFlowStatusResponse
import kr.example.eda_test.application.port.`in`.CreateGoodsCommand
import kr.example.eda_test.config.TemporalConfig
import kr.example.eda_test.temporal.workflow.GoodsWorkflow
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/temporal/goods")
class TemporalGoodsController(
    private val workflowClient: WorkflowClient
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createGoodsWithTemporal(@RequestBody request: CreateGoodsRequest): CreateGoodsResponse {
        val command = CreateGoodsCommand(
            name = request.name,
            price = request.price,
        )

        // 워크플로우 ID 생성
        val workflowId = "goods-creation-${UUID.randomUUID()}"

        // 워크플로우 옵션 설정
        val options = WorkflowOptions.newBuilder()
            .setWorkflowId(workflowId)
            .setTaskQueue(TemporalConfig.TASK_QUEUE)
            .build()

        // 워크플로우 스텁 생성
        val workflow = workflowClient.newWorkflowStub(GoodsWorkflow::class.java, options)

        // 워크플로우 비동기 실행
        WorkflowClient.start(workflow::createGoods, command)

        return CreateGoodsResponse(
            workflowId = workflowId,
            message = "Goods creation workflow started",
        )
    }

    @GetMapping("/{workflowId}")
    fun getWorkflowStatus(@PathVariable workflowId: String): GetWorkFlowStatusResponse {
        // 워크플로우 스텁 가져오기
        val workflow = workflowClient.newWorkflowStub(GoodsWorkflow::class.java, workflowId)

        return try {
            // 워크플로우 결과 조회 (완료된 경우)
            val result = workflow.createGoods(CreateGoodsCommand("", java.math.BigDecimal.ZERO))
            GetWorkFlowStatusResponse(
                workflowId = workflowId,
                status = "COMPLETED",
                result = result
            )
        } catch (e: Exception) {
            GetWorkFlowStatusResponse(
                workflowId = workflowId,
                status = "RUNNING or FAILED",
                result = "Use Temporal UI to check detailed status"
            )
        }
    }
}
