package com.daomingedu.onecpteacher.mvp.model

import android.app.Application

import com.daomingedu.onecpteacher.mvp.contract.UpdateVideoContract
import com.daomingedu.onecpteacher.mvp.model.api.service.CustomerService
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.CompareFaceScoreBean
import com.daomingedu.onecpteacher.mvp.model.entity.UploadVideoParamBean
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by jianhongxu on 3/18/21
 * Description
 */
@ActivityScope
class UpdateVideoModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    UpdateVideoContract.Model {

    override fun getCompareFaceScore(
        sessionId: String,
        testSignId: String,
        base64Image: String
    ): Observable<BaseJson<CompareFaceScoreBean>> {
        return mRepositoryManager.obtainRetrofitService(CustomerService::class.java)
            .getCompareFaceScore(sessionId, testSignId, base64Image)
    }

    override fun getUpdateVideoParam(
        sessionId: String,
        testSignId: String
    ): Observable<BaseJson<UploadVideoParamBean>> {
        return mRepositoryManager.obtainRetrofitService(CustomerService::class.java)
            .getUploadVideoParam(sessionId, testSignId)
    }

    override fun saveUploadVideo(
        sessionId: String,
        testSignId: String,
        videoPath: String,
        videoSize: String,
        isAudit: Int,
        catalogId:String
    ): Observable<BaseJson<Any>> {
        return mRepositoryManager.obtainRetrofitService(CustomerService::class.java)
            .saveUploadVideo(sessionId, testSignId, videoPath, videoSize, isAudit,catalogId)
    }

    @Inject
    lateinit var mGson: Gson;

    @Inject
    lateinit var mApplication: Application;


}