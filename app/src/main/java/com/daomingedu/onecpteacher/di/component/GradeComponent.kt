package com.daomingedu.onecpteacher.di.component

import com.daomingedu.onecpteacher.di.module.GradeModule
import com.daomingedu.onecpteacher.di.module.TestModule
import com.daomingedu.onecpteacher.mvp.ui.GradeActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Component

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@ActivityScope
@Component(modules = [GradeModule::class],dependencies = [AppComponent::class])
interface GradeComponent {
    fun inject(activity: GradeActivity)
}