package com.daomingedu.onecpteacher.mvp.model.entity

import java.io.Serializable

/**
 * 基类实体
 * Created by cd on 2018/8/23.
 */

class BaseJson<T> : Serializable {
    var code: Int = 0
    var count: Int = 0
    var msg: String = ""
    var data: T? = null
}
