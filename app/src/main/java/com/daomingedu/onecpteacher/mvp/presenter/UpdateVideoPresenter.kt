package com.daomingedu.onecpteacher.mvp.presenter

import android.app.Application
import com.daomingedu.onecpteacher.app.Constant
import com.daomingedu.onecpteacher.app.Preference
import com.daomingedu.onecpteacher.mvp.contract.UpdateVideoContract
import com.daomingedu.onecpteacher.mvp.model.api.Api
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.CompareFaceScoreBean
import com.daomingedu.onecpteacher.mvp.model.entity.UploadVideoParamBean
import com.daomingedu.onecpteacher.util.RxUtils
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import java.util.*
import javax.inject.Inject

/**
 * Created by jianhongxu on 3/18/21
 * Description
 */
@ActivityScope
class UpdateVideoPresenter
@Inject
constructor(model: UpdateVideoContract.Model, view: UpdateVideoContract.View) :
    BasePresenter<UpdateVideoContract.Model, UpdateVideoContract.View>(model, view) {

    @Inject
    lateinit var mErrorHandler: RxErrorHandler

    @Inject
    lateinit var mApplication: Application

    @Inject
    lateinit var mImageLoader: ImageLoader

    @Inject
    lateinit var mAppManager: AppManager

    private val sessionId: String by Preference(Constant.SESSIONID, "")

    /**
     * 获取人脸识别
     */
    fun getCompareFaceScore(testSignId: String, base64Image: String) {
        mModel.getCompareFaceScore(sessionId, testSignId, base64Image)
            .retryWhen(RetryWithDelay(2, 5))
            .compose(RxUtils.applySchedulers(mRootView))
            .subscribe(object :
                ErrorHandleSubscriber<BaseJson<CompareFaceScoreBean>>(mErrorHandler) {
                override fun onNext(t: BaseJson<CompareFaceScoreBean>) {
                    if (Api.SUCCESS == t.code) {
                        t.data?.let { mRootView.requestCompareFaceScoreSuccess(it) }
                    } else {
                        mRootView.showMessage(t.msg)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    mRootView.showMessage(t.toString())
                }

            })

    }

    /**
     * 获取上传时需要的参数
     */
    fun getUploadVideoParam(testSignId: String) {
        mModel.getUpdateVideoParam(sessionId, testSignId)
            .retryWhen(RetryWithDelay(2, 5))
            .compose(RxUtils.applySchedulers(mRootView))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :
                ErrorHandleSubscriber<BaseJson<UploadVideoParamBean>>(mErrorHandler) {
                override fun onNext(t: BaseJson<UploadVideoParamBean>) {
                    if (Api.SUCCESS == t.code) {
                        t.data?.let { mRootView.requestUploadVideoParam(it) }
                    } else {
                        mRootView.requestUploadVideoParamError(t.msg)
                        mRootView.showMessage(t.msg)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    mRootView.showMessage(t.toString())
                }

            })
    }


    /**
     * 上传腾讯云后保存视频
     */
    fun saveUploadVideo(
        testSignId: String,
        videoPath: String,
        videoSize: String,
        isAudit: Int,
        catalogId:String
    ) {
        mModel.saveUploadVideo(sessionId,testSignId, videoPath, videoSize, isAudit,catalogId)
            .retryWhen(RetryWithDelay(2, 5))
            .compose(RxUtils.applySchedulers(mRootView))
            .subscribe(object :ErrorHandleSubscriber<BaseJson<Any>>(mErrorHandler){
                override fun onNext(t: BaseJson<Any>) {
                    if (Api.SUCCESS == t.code) {
                        mRootView.requestSaveUploadVideo()

                    } else {
                        mRootView.showMessage(t.msg)
                    }

                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    mRootView.showMessage(t.toString())
                }
            })

    }


}