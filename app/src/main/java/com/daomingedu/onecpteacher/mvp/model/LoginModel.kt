package com.daomingedu.onecpteacher.mvp.model

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.ActivityScope
import javax.inject.Inject



import com.daomingedu.onecpteacher.mvp.contract.LoginContract
import com.daomingedu.onecpteacher.mvp.model.api.service.CustomerService
import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.RegisterBean
import com.daomingedu.onecpteacher.mvp.model.entity.VersionBean
import io.reactivex.Observable


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/24/2019 23:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class LoginModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager),
    LoginContract.Model {
    override fun login(
        key: String,
        mobile: String,
        password: String,
        versionCode: Int
    ): Observable<BaseJson<RegisterBean>> {
        return mRepositoryManager.obtainRetrofitService(CustomerService::class.java)
            .login(key, mobile, password,  versionCode)
    }

    override fun getVersionInfo(key: String): Observable<BaseJson<VersionBean>> {
        return mRepositoryManager.obtainRetrofitService(CustomerService::class.java)
            .getVersionInfo(key)
    }


    @Inject
    lateinit var mGson: Gson;

    @Inject
    lateinit var mApplication: Application;

    override fun onDestroy() {
        super.onDestroy();
    }
}
