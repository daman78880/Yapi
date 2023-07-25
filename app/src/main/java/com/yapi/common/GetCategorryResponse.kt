package com.yapi.common

data class GetCategorryResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)

data class Data(
    val _id: String,
    val category_name: String,
    val count: Int,
    val order_id: Int
)