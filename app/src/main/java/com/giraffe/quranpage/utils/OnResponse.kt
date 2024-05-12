package com.giraffe.quranpage.utils

interface OnResponse {
    fun onSuccess(result:String)
    fun onFail(errorMsg:String)
}