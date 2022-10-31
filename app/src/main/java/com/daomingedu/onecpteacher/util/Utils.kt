package com.daomingedu.onecpteacher.util

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        lateinit var app: Application

        fun encodeImage(bitmap: Bitmap): String {
            val size = bitmap.width * bitmap.height * 4

            // 创建一个字节数组输出流,流的大小为size
            val baos = ByteArrayOutputStream(size)
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            // 将字节数组输出流转化为字节数组byte[]
            val imagedata = baos.toByteArray()

            return Base64.encodeToString(imagedata, Base64.DEFAULT)
        }

        fun decodeImage(data: String?): Bitmap? {
            if (data == null) {
                return null
            }
            val input = Base64.decode(data, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(input, 0, input.size)
            return bitmap
        }


        fun getTime(date_temp: Long, dateFormat: String): String? {
            //时间格式,HH是24小时制，hh是AM PM12小时制
            val sdf = SimpleDateFormat(dateFormat)
            //比如timestamp=1449210225945；
            val date_string = sdf.format(Date(date_temp))
            //至于取10位或取13位，date_temp*1000L就是这种截取作用。如果是和服务器传值的，就和后台商量好就可以了
            Log.d("test", date_string)
            return date_string
        }
    }
}