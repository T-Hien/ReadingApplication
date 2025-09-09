package vn.example.ReadingAPI.controller.Model

data class ResponseData(var status: Int = 0,
                        var message: String = "",
                        var data: Any? = null,
                        var dataNum:Int = 0,
                        var dataList: List<Any>? = emptyList())
