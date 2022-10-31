package com.daomingedu.onecpteacher.mvp.presenter

import android.app.Application
import com.daomingedu.onecpteacher.app.Constant
import com.daomingedu.onecpteacher.app.Preference
import com.daomingedu.onecpteacher.mvp.contract.StudentListContract
import com.daomingedu.onecpteacher.mvp.model.api.Api
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.StudentListBean
import com.daomingedu.onecpteacher.mvp.ui.adapter.StudentListAdapter
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
class StudentListPresenter
@Inject
constructor(model: StudentListContract.Model, rootView: StudentListContract.View) :
    BasePresenter<StudentListContract.Model, StudentListContract.View>(model, rootView) {

    @Inject
    lateinit var mErrorHandler: RxErrorHandler

    @Inject
    lateinit var mApplication: Application

    @Inject
    lateinit var mImageLoader: ImageLoader

    @Inject
    lateinit var mAppManager: AppManager
    @Inject
    lateinit var mDatas: MutableList<StudentListBean>

    @Inject
    lateinit var mAdapter: StudentListAdapter

    private val sessionId: String by Preference(Constant.SESSIONID, "")

    private var pageStart = 0
    val pageSize = 20

    fun getStudentList(pullToRefresh: Boolean,testId:String,classId:String){
        if (pullToRefresh) {
            pageStart = 0
        }

        mModel.getStudentList(sessionId,testId, classId, pageStart, pageSize)
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryWithDelay(2, 5))
            .doOnSubscribe {
                if (pullToRefresh) {
                    mRootView.showLoading()
                }
            }.subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                if (pullToRefresh){
                    mRootView.hideLoading()
                }
            }
            .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
            .subscribe(object : ErrorHandleSubscriber<BaseJson<MutableList<StudentListBean>>>(mErrorHandler){
                override fun onNext(t: BaseJson<MutableList<StudentListBean>>) {
                    if (t.code == Api.SUCCESS) {
                        if (t.data != null) {
                            if (pullToRefresh) {
                                mDatas.clear()
                                mDatas.addAll(t.data!!)
                                mAdapter.notifyDataSetChanged()
                            } else {
                                mDatas.addAll(t.data!!)
                                mAdapter.notifyItemRangeChanged(mDatas.size - t.data!!.size, mDatas.size)
                            }
                            if (t.data!!.size < pageSize) {
                                mAdapter.loadMoreEnd()
                            } else {
                                mAdapter.loadMoreComplete()
                                pageStart += pageSize
                            }
                        }
                    } else {
                        mRootView.showMessage(t.msg)
                    }
                }

            })
    }

}