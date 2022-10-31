package com.daomingedu.onecpteacher.di.component

import com.daomingedu.onecpteacher.di.module.StudentListModule
import com.daomingedu.onecpteacher.mvp.ui.StudentListActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Component

/**
* Description
* Created by jianhongxu on 2021/10/21
*/
@ActivityScope
@Component(modules = [StudentListModule::class],dependencies = [AppComponent::class])
interface StudentListComponent {
    fun inject(activity: StudentListActivity)
}