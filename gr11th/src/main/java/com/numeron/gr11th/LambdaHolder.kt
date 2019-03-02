package com.numeron.gr11th


class LambdaHolder<T>(
        //申请权限时、启动Activity返回时或其它操作成功时的回调
        val onSuccess: (T) -> Unit) {

    //当启动Activity后由用户自定义操作触发时的回调
    private var onDefined: () -> Unit = {}
    //当启动Activity后取消时的回调
    private var onCanceled: () -> Unit = {}
    //仅在申请被获取时调用
    private var onDenied: (List<String>) -> Unit = {}

    fun setOnCanceledCallback(callback: () -> Unit): LambdaHolder<T> {
        onCanceled = callback
        return this
    }

    fun setOnDeniedCallback(callback: (List<String>) -> Unit): LambdaHolder<T> {
        onDenied = callback
        return this
    }

    fun setOnDefinedCallback(callback: () -> Unit): LambdaHolder<T> {
        onDefined = callback
        return this
    }

    fun onDenied(list: List<String>) = this.onDenied.invoke(list)

    fun onDefined() = this.onDefined.invoke()

    fun onCanceled() = this.onCanceled.invoke()


}

/*
fun <T> LambdaHolder<T>.setCanceledCallback(callback: () -> Unit): LambdaHolder<T> {
    onCanceled = callback
    return this
}

fun <T> LambdaHolder<T>.setDeniedCallback(callback: (List<String>) -> Unit): LambdaHolder<T> {
    onDenied = callback
    return this
}

fun <T> LambdaHolder<T>.setDefinedCallback(callback: () -> Unit): LambdaHolder<T> {
    onDefined = callback
    return this
}*/
