package com.daomingedu.onecpteacher.di.component

import com.daomingedu.onecpteacher.di.module.TestModule
import com.daomingedu.onecpteacher.mvp.ui.TestActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Component

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@ActivityScope
@Component(modules = [TestModule::class],dependencies = [AppComponent::class])
interface TestComponent {
    fun inject(activity:TestActivity)
}