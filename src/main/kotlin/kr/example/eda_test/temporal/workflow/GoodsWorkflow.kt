package kr.example.eda_test.temporal.workflow

import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod
import kr.example.eda_test.application.port.`in`.CreateGoodsCommand

@WorkflowInterface
interface GoodsWorkflow {

    @WorkflowMethod
    fun createGoods(command: CreateGoodsCommand): String
}
