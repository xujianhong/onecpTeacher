package com.daomingedu.onecpteacher.mvp.model.entity

/**
 * Created by jianhongxu on 3/19/21
 * Description
 */
data class UploadVideoParamBean(
    val uploadKey: String,
    val bucketName: String,
    val cosTempKey: CosTempKey,
    val region: String,
    val remark: String,
    val textbook: MutableList<TextBook>
)

data class TextBook(val name: String, val catalogList: MutableList<CatalogList>)

data class CatalogList(val name: String, val id: String)


data class CosTempKey(
    val credentials: Credentials,
    val expiration: String,
    val expiredTime: Int,
    val requestId: String,
    val startTime: Int
)

data class Credentials(
    val sessionToken: String,
    val tmpSecretId: String,
    val tmpSecretKey: String
)