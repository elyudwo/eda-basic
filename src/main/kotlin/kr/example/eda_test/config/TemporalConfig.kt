package kr.example.eda_test.config

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import io.temporal.worker.WorkerFactory
import jakarta.annotation.PreDestroy
import kr.example.eda_test.temporal.activity.GoodsActivitiesImpl
import kr.example.eda_test.temporal.workflow.GoodsWorkflowImpl
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemporalConfig(
    private val goodsActivities: GoodsActivitiesImpl
) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private var workerFactoryInstance: WorkerFactory? = null

    companion object {
        const val TASK_QUEUE = "GOODS_TASK_QUEUE"
    }

    @Bean
    fun workflowServiceStubs(): WorkflowServiceStubs {
        val options = WorkflowServiceStubsOptions.newBuilder()
            .setTarget("localhost:7233")
            .build()

        return WorkflowServiceStubs.newServiceStubs(options)
    }

    @Bean
    fun workflowClient(workflowServiceStubs: WorkflowServiceStubs): WorkflowClient {
        val options = WorkflowClientOptions.newBuilder()
            .setNamespace("default")
            .build()

        return WorkflowClient.newInstance(workflowServiceStubs, options)
    }

    @Bean
    fun workerFactory(workflowClient: WorkflowClient): WorkerFactory {
        logger.info("Creating and starting Temporal worker...")

        val factory = WorkerFactory.newInstance(workflowClient)
        workerFactoryInstance = factory

        // Worker 생성 및 등록
        val worker = factory.newWorker(TASK_QUEUE)

        // 워크플로우 등록
        worker.registerWorkflowImplementationTypes(GoodsWorkflowImpl::class.java)

        // 액티비티 등록
        worker.registerActivitiesImplementations(goodsActivities)

        // Worker 시작
        factory.start()
        logger.info("Temporal worker started successfully")

        return factory
    }

    @PreDestroy
    fun shutdownWorker() {
        logger.info("Shutting down Temporal worker...")
        workerFactoryInstance?.shutdown()
    }
}
