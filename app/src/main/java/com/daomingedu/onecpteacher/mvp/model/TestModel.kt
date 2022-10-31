package com.daomingedu.onecpteacher.mvp.model

import com.daomingedu.onecpteacher.mvp.contract.TestContract
import com.daomingedu.onecpteacher.mvp.model.api.service.CustomerService
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.TestBean
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
class TestModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    TestContract.Model {
    override fun getTest(sessionId: String): Observable<BaseJson<MutableList<TestBean>>> {
        return mRepositoryManager.obtainRetrofitService(CustomerService::class.java).getTests(sessionId)
    }
    @Inject
    lateinit var mGson: Gson;

}