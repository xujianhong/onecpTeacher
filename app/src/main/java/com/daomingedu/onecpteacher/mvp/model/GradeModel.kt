package com.daomingedu.onecpteacher.mvp.model

import com.daomingedu.onecpteacher.mvp.contract.GradeContract
import com.daomingedu.onecpteacher.mvp.model.api.service.CustomerService
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.GradeBean
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@ActivityScope
class GradeModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    GradeContract.Model {
    override fun getGrade(
        sessionId: String,
        testId: String
    ): Observable<BaseJson<MutableList<GradeBean>>> {
        return mRepositoryManager.obtainRetrofitService(CustomerService::class.java)
            .getGrade(sessionId, testId)
    }

    @Inject
    lateinit var mGson: Gson;

}