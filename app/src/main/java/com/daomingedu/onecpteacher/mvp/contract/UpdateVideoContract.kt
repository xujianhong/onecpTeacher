package com.daomingedu.onecpteacher.mvp.contract


import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.CompareFaceScoreBean
import com.daomingedu.onecpteacher.mvp.model.entity.UploadVideoParamBean
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable

/**
 * Created by jianhongxu on 3/18/21
 * Description
 */
interface UpdateVideoContract {
    interface View : IView {
        fun requestCompareFaceScoreSuccess(bean: CompareFaceScoreBean)
        fun requestUploadVideoParam(bean: UploadVideoParamBean)
        fun requestUploadVideoParamError(msg: String)
        fun requestSaveUploadVideo()
    }

    interface Model : IModel {

        fun getCompareFaceScore(
            sessionId: String,
            testSignId: String,
            base64Image: String
        ): Observable<BaseJson<CompareFaceScoreBean>>

        fun getUpdateVideoParam(
            sessionId: String,
            testSignId: String
        ): Observable<BaseJson<UploadVideoParamBean>>

        fun saveUploadVideo(
            sessionId: String,
            testSignId: String,
            videoPath: String,
            videoSize: String,
            isAudit: Int,
            catalogId:String
        ): Observable<BaseJson<Any>>
    }
}