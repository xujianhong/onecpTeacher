package com.daomingedu.onecpteacher.mvp.presenter

import android.app.Application
import com.daomingedu.onecpteacher.app.Constant
import com.daomingedu.onecpteacher.app.Preference
import com.daomingedu.onecpteacher.mvp.contract.StudentTestInfoContract
import com.daomingedu.onecpteacher.mvp.model.api.Api
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.RegisterBean
import com.daomingedu.onecpteacher.mvp.model.entity.StudentTestInfoBean
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
class StudentTestInfoPresenter
@Inject
constructor(model: StudentTestInfoContract.Model, rootView: StudentTestInfoContract.View) :
    BasePresenter<StudentTestInfoContract.Model, StudentTestInfoContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler

    @Inject
    lateinit var mApplication: Application

    @Inject
    lateinit var mImageLoader: ImageLoader

    @Inject
    lateinit var mAppManager: AppManager


    private val sessionId: String by Preference(Constant.SESSIONID, "")


    fun getStudentTestInfo(testId: String, studentId: String, testSignId: String) {
        mModel.getStudentTestInfo(sessionId, testId, studentId, testSignId)
            .retryWhen(RetryWithDelay(2, 5))
            .compose(RxUtils.applySchedulers(mRootView))
            .subscribe(object :
                ErrorHandleSubscriber<BaseJson<StudentTestInfoBean>>(mErrorHandler) {
                override fun onNext(t: BaseJson<StudentTestInfoBean>) {
                    if (Api.SUCCESS == t.code) {
                        t.data?.let { mRootView.requestStudentTestSuccess(it) }
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