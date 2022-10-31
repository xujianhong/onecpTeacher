package com.daomingedu.onecpteacher.mvp.model

import com.daomingedu.onecpteacher.mvp.contract.StudentListContract
import com.daomingedu.onecpteacher.mvp.model.api.service.CustomerService
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.StudentListBean
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
class StudentListModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    StudentListContract.Model {
    override fun getStudentList(
        sessionId: String,
        testId: String,
        classId: String,
        start: Int,
        size: Int
    ): Observable<BaseJson<MutableList<StudentListBean>>> {
        return mRepositoryManager.obtainRetrofitService(CustomerService::class.java).getStudentList(sessionId, testId, classId, start, size)
    }

    @Inject
    lateinit var mGson: Gson;
}