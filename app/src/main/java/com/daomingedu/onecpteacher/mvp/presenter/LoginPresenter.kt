package com.daomingedu.onecpteacher.mvp.presenter

import android.app.Application
import com.daomingedu.onecpteacher.BuildConfig
import com.daomingedu.onecpteacher.app.Constant
import com.daomingedu.onecpteacher.app.Preference
import com.daomingedu.onecpteacher.mvp.contract.LoginContract
import com.daomingedu.onecpteacher.mvp.model.api.Api
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.RegisterBean
import com.daomingedu.onecpteacher.mvp.model.entity.VersionBean
import com.daomingedu.onecpteacher.util.RxUtils
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@ActivityScope
class LoginPresenter
@Inject
constructor(model:LoginContract.Model,rootView:LoginContract.View):
BasePresenter<LoginContract.Model,LoginContract.View>(model, rootView){
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    private var sessionId by Preference(Constant.SESSIONID,"")

    private var videoTime by Preference(Constant.VIDEO_TIME,0)
    private var videoPixel by Preference(Constant.VIDEO_PIXEL,0)


    fun login(mobile:String,password:String){
        mModel.login(Constant.KEY,mobile,password, BuildConfig.VERSION_CODE)
            .retryWhen(RetryWithDelay(2, 5))
            .compose(RxUtils.applySchedulers(mRootView))
            .subscribe(object : ErrorHandleSubscriber<BaseJson<RegisterBean>>(mErrorHandler) {
                override fun onNext(t: BaseJson<RegisterBean>) {
                    if (Api.SUCCESS == t.code) {
                        sessionId = t.data?.sessionId?:""
                        mRootView.requestLoginSuccess()
                    }else{
                        mRootView.showMessage(t.msg)
                    }
                }

                override fun onError(t: Throwable) {
                    super.onError(t)
                    mRootView.showMessage(t.toString())
                }
            })
    }

    fun getVersionInfo(){
        mModel.getVersionInfo(Constant.KEY)
            .compose(RxUtils.applySchedulers(mRootView))
            .subscribe(object : ErrorHandleSubscriber<BaseJson<VersionBean>>(mErrorHandler){
                override fun onNext(t: BaseJson<VersionBean>) {
                    if (t.code == Api.SUCCESS) {
                        videoTime = t.data?.videoTime ?: 0
                        videoPixel = t.data?.videoPixel ?: 0
                        mRootView.requestVersionInfoSuccess(t.data!!)
                    }else{
                        mRootView.showMessage(t.msg)
                    }
                }
            })
    }
}