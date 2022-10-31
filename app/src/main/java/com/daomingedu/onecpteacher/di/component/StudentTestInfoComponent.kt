package com.daomingedu.onecpteacher.di.component

import com.daomingedu.onecpteacher.di.module.StudentTestInfoModule
import com.daomingedu.onecpteacher.mvp.ui.StudentTestInfoActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Component

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@ActivityScope
@Component(modules = [StudentTestInfoModule::class],dependencies = [AppComponent::class])
interface StudentTestInfoComponent {
    fun inject(activity:StudentTestInfoActivity)
}