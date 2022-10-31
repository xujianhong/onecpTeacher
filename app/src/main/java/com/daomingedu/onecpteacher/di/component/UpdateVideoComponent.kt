package com.daomingedu.onecpteacher.di.component


import com.daomingedu.onecpteacher.di.module.UpdateVideoModule
import com.daomingedu.onecpteacher.mvp.ui.UploadVideoActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Component

/**
 * Created by jianhongxu on 3/18/21
 * Description
 */
@ActivityScope
@Component(modules = [UpdateVideoModule::class],dependencies = [AppComponent::class])
interface UpdateVideoComponent {
    fun inject(activity:UploadVideoActivity)
}