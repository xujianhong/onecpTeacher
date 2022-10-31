package com.daomingedu.onecpteacher.di.module

import com.daomingedu.onecpteacher.mvp.contract.GradeContract
import com.daomingedu.onecpteacher.mvp.model.GradeModel
import com.daomingedu.onecpteacher.mvp.model.entity.GradeBean
import com.daomingedu.onecpteacher.mvp.model.entity.TestBean
import com.daomingedu.onecpteacher.mvp.ui.adapter.GradeAdapter
import com.daomingedu.onecpteacher.mvp.ui.adapter.TestAdapter
import com.jess.arms.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@Module
class GradeModule(private val view:GradeContract.View) {

    @ActivityScope
    @Provides
    fun provideGradeView():GradeContract.View{
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideGradeModel(model:GradeModel):GradeContract.Model{
        return model
    }

    @ActivityScope
    @Provides
    fun provideDatas() = mutableListOf<GradeBean>()


    @ActivityScope
    @Provides
    fun provideGradeAdapter(datas:MutableList<GradeBean>) = GradeAdapter(datas)
}