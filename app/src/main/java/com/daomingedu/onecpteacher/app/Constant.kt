package com.daomingedu.onecpteacher.app

import android.os.Environment
import com.daomingedu.onecpteacher.mvp.model.api.Api

import java.io.File

/**
 * @创建者 chendong
 * @创建时间 2019/2/22 9:58
 * @描述
 */
interface Constant {
    companion object {
        const val KEY = "5561a18d884e44a6847a1ca96b2fb075"

        //        const val KEY = "0afe0ff023194bcfb8c90a50f92c8bcd"
        const val SESSIONID = "sessionId"
        const val MOBILE = "mobile"
        const val VIDEO_TIME = "video_time" //录制时长限制(单位分钟)
        const val VIDEO_PIXEL = "video_pixel" //录制视频的像素(1:480p,2:720p,3:1080p)


        /**
         * 文件存储对象前缀
         */
        const val COS_PATH = "cosPath"
        const val URL_EXTRA = "url_extra"
        const val TITLE_EXTRA = "title_extra"
        const val TEST_TYPE_EXTRA = "test_type_extra"
        const val TEST_POSITION_EXTRA = "test_position_extra"
        const val TEST_CITIES_EXTRA = "test_cities_extra"
        const val TEST_TITLE_EXTRA = "test_title_extra"

        const val PROVINCE_EXTRA = "province_extra"
        const val CITY_EXTRA = "CITY_extra"
        const val REGION_EXTRA = "region_extra"
        const val PROVINCE_SHOW_EXTRA = "province_show_extra"
        const val PROVINCE_TURN_EXTRA = 0x01
        const val PROVINCE_TIME_EXTRA = "province_time_extra"

        const val SCHOOL_TURN_EXTRA = 0x02
        const val SCHOOL_NUMBER_EXTRA = "school_number_extra"
        const val SCHOOL_NAME_EXTRA = "school_name_extra"

        //传递评测列表id
        const val ENROLL_TEST_EXTRA = "enroll_test_extra"

        //传递评测列表报名后的testSignId
        const val ENROLL_ID_EXTRA = "enroll_id_extra"


        const val TYPE_PHOTO = 1//图片分享
        const val TYPE_RECORD = 2//录音分享
        const val TYPE_VIDEO = 3//视频分享
        const val ISMY_TRUE = 1//我的分享
        const val ISMY_FALSE = 0//全部分享
        const val IMAGE_PREFIX =
            "https://talentgame-1255518711.cos.ap-chongqing.myqcloud.com/"//图片前缀

        const val REGISTER_PROTOCOL = "http://4hand.com.cn/art.html" //注册协议

        val UPLOAD_ID_PHOTO = Api.APP_DOMAIN + "/studentPhoto?sessionId=" //上传证件照
        val SAVE_TEMP_IMG =
            Environment.getExternalStorageDirectory().toString() + File.separator + "temp/"//临时文件
    }
}