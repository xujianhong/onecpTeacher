package com.daomingedu.onecpteacher.di.component


import com.daomingedu.onecpteacher.di.module.LoginModule
import com.daomingedu.onecpteacher.mvp.ui.LoginActivity
import dagger.Component
import com.jess.arms.di.component.AppComponent



import com.jess.arms.di.scope.ActivityScope



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
@Component(modules = [LoginModule::class], dependencies = [AppComponent::class])
interface LoginComponent {
    fun inject(activity: LoginActivity)
}
