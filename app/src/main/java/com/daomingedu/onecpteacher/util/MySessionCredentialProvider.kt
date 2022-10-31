package com.daomingedu.onecpteacher.util

import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials
import com.tencent.qcloud.core.auth.SessionQCloudCredentials


class MySessionCredentialProvider(val secretId: String, val secretKey: String, val token: String, val startTime: Long, val expiredTime: Long)
    : BasicLifecycleCredentialProvider() {

    override fun fetchNewCredentials(): QCloudLifecycleCredentials {
        return SessionQCloudCredentials(secretId, secretKey, token, startTime, expiredTime)
    }
}