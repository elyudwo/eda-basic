package kr.example.eda_test.adapter.out

data class GetWorkFlowStatusResponse(
    val workflowId: String,
    val status: String,
    val result: String
)
