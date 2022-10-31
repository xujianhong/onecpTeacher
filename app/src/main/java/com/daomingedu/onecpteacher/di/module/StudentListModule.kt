package com.daomingedu.onecpteacher.di.module

import com.daomingedu.onecpteacher.mvp.contract.StudentListContract
import com.daomingedu.onecpteacher.mvp.model.StudentListModel
import com.daomingedu.onecpteacher.mvp.model.entity.StudentListBean
import com.daomingedu.onecpteacher.mvp.ui.adapter.StudentListAdapter
import com.jess.arms.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@Module
class StudentListModule(private val view:StudentListContract.View) {

    @ActivityScope
    @Provides
    fun provideStudentListView():StudentListContract.View{
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideStudentListModel(model:StudentListModel):StudentListContract.Model{
        return model
    }

    @ActivityScope
    @Provides
    fun provideDatas() = mutableListOf<StudentListBean>()


    @ActivityScope
    @Provides
    fun provideGradeAdapter(datas:MutableList<StudentListBean>) = StudentListAdapter(datas)
}