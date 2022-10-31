package com.daomingedu.onecpteacher.app


import com.daomingedu.onecpteacher.app.`interface`.ProgressListener
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.File

/**
 * @创建者 chendong
 * @创建时间 2019/2/23 9:36
 * @描述
 */
class UploadFileRequestBody(uploadFile: File, var progressListener: ProgressListener?=null) : RequestBody() {
    private var mRequestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile)
    var bufferedSink: BufferedSink? = null

    override fun contentType(): MediaType? {
        return mRequestBody.contentType()
    }

    override fun contentLength(): Long {
        return mRequestBody.contentLength()
    }

    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink))
        }
        if (bufferedSink != null) {
            //写入
            mRequestBody.writeTo(bufferedSink!!)
            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink?.flush()
        }
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            var bytesWritten = 0L
            var contentLength = 0L
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength == 0L) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                //回调上传接口
                progressListener?.onProgress(bytesWritten, contentLength, bytesWritten == contentLength)
            }
        }
    }

}