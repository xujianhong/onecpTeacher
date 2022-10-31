package com.daomingedu.onecpteacher.di.module

import com.daomingedu.onecpteacher.mvp.contract.StudentTestInfoContract
import com.daomingedu.onecpteacher.mvp.model.StudentTestInfoModel
import com.jess.arms.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@Module
class StudentTestInfoModule (private val view:StudentTestInfoContract.View){

    @ActivityScope
    @Provides
    fun provideStudentTestInfoView():StudentTestInfoContract.View{
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideStudentTestInfoModel(model:StudentTestInfoModel):StudentTestInfoContract.Model{
        return model
    }
}