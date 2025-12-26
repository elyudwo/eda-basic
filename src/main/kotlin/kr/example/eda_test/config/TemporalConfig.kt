package kr.example.eda_test.config

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import io.temporal.worker.Worker
import io.temporal.worker.WorkerFactory
import jakarta.annotation.PostConstruct
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
        return WorkerFactory.newInstance(workflowClient)
    }

    @Bean
    fun goodsWorker(workerFactory: WorkerFactory): Worker {
        val worker = workerFactory.newWorker(TASK_QUEUE)

        // 워크플로우 등록
        worker.registerWorkflowImplementationTypes(GoodsWorkflowImpl::class.java)

        // 액티비티 등록
        worker.registerActivitiesImplementations(goodsActivities)

        return worker
    }

    @PostConstruct
    fun startWorker() {
        logger.info("Starting Temporal worker...")
        val workerFactory = workerFactory(workflowClient(workflowServiceStubs()))
        goodsWorker(workerFactory)
        workerFactory.start()
        logger.info("Temporal worker started successfully")
    }

    @PreDestroy
    fun shutdownWorker() {
        logger.info("Shutting down Temporal worker...")
        workflowServiceStubs().shutdown()
    }
}
