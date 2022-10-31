package com.daomingedu.onecpteacher.mvp.model.entity

/**
 * @param versionName 版本名称
 * @param versionCode 版本号
 * @param remark 版本描述
 * @param path Android的下载地址
 * @param isMust 0不强制1必须更新
 * @param isOpenShare 师傅开启朋友圈(1是0否)
 * @param videoTime 录制时长限制(单位分钟)
 * @param videoPixel 录制视频的像素(1:480p,2:720p,3:1080p)
 */
data class VersionBean(
    val versionName: String,
    val versionCode: Int,
    val remark: String,
    val path: String,
    val isMust: Int,
    val isOpenShare: Int,
    val videoTime: Int,
    val videoPixel: Int,
    val isShowButton: Int,
    val isShowFolder: Int,
    val isQuestionBank: Int
)