package com.daomingedu.onecpteacher.mvp.model

import com.daomingedu.onecpteacher.mvp.contract.StudentListContract
import com.daomingedu.onecpteacher.mvp.contract.StudentTestInfoContract
import com.daomingedu.onecpteacher.mvp.model.api.service.CustomerService
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.StudentTestInfoBean
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
class StudentTestInfoModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    StudentTestInfoContract.Model {
    override fun getStudentTestInfo(
        sessionId: String,
        testId: String,
        studentId: String,
        testSignId: String
    ): Observable<BaseJson<StudentTestInfoBean>> {
        return mRepositoryManager.obtainRetrofitService(CustomerService::class.java)
            .getStudentTestInfo(sessionId, testId, studentId, testSignId)
    }

    @Inject
    lateinit var mGson: Gson;
}