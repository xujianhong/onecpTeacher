package com.daomingedu.onecpteacher.di.module

import com.daomingedu.onecpteacher.mvp.contract.LoginContract
import com.daomingedu.onecpteacher.mvp.model.LoginModel
import com.jess.arms.di.scope.ActivityScope
import dagger.Module
import dagger.Provides


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
@Module
//构建LoginModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class LoginModule(private val view: LoginContract.View) {
    @ActivityScope
    @Provides
    fun provideLoginView(): LoginContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideLoginModel(model: LoginModel): LoginContract.Model {
        return model
    }
}
