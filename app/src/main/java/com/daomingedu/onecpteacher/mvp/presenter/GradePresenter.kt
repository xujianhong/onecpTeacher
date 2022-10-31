package com.daomingedu.onecpteacher.mvp.presenter

import android.app.Application
import com.daomingedu.onecpteacher.app.Constant
import com.daomingedu.onecpteacher.app.Preference
import com.daomingedu.onecpteacher.mvp.contract.GradeContract
import com.daomingedu.onecpteacher.mvp.model.api.Api
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.GradeBean
import com.daomingedu.onecpteacher.mvp.model.entity.TestBean
import com.daomingedu.onecpteacher.mvp.ui.adapter.GradeAdapter
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@ActivityScope
class GradePresenter
@Inject
constructor(model: GradeContract.Model, rootView: GradeContract.View) :
    BasePresenter<GradeContract.Model, GradeContract.View>(model, rootView) {

    @Inject
    lateinit var mErrorHandler: RxErrorHandler

    @Inject
    lateinit var mApplication: Application

    @Inject
    lateinit var mImageLoader: ImageLoader

    @Inject
    lateinit var mAppManager: AppManager
    @Inject
    lateinit var mDatas: MutableList<GradeBean>

    @Inject
    lateinit var mAdapter: GradeAdapter

    private val sessionId: String by Preference(Constant.SESSIONID, "")

    fun getTest(testId:String){
        mModel.getGrade(sessionId,testId)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(2, 5))
            .doOnSubscribe {
                mRootView.showLoading()

            }.subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                mRootView.hideLoading()

            }
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
            .subscribe(object :
                ErrorHandleSubscriber<BaseJson<MutableList<GradeBean>>>(mErrorHandler) {
                override fun onNext(t: BaseJson<MutableList<GradeBean>>) {
                    if (Api.SUCCESS == t.code) {
                        if (t.data != null) {
                            mDatas.clear()
                            mDatas.addAll(t.data!!)
                            mAdapter.notifyDataSetChanged()
                            mRootView.requestGradeSuccess()
                        }
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