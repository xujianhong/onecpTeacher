package com.daomingedu.onecpteacher.di.module

import com.daomingedu.onecpteacher.mvp.contract.TestContract
import com.daomingedu.onecpteacher.mvp.model.TestModel
import com.daomingedu.onecpteacher.mvp.model.entity.TestBean
import com.daomingedu.onecpteacher.mvp.ui.adapter.TestAdapter
import com.jess.arms.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
@Module
class TestModule(private  val view:TestContract.View){
    @ActivityScope
    @Provides
    fun provideTestView():TestContract.View{
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideTestModel(model:TestModel):TestContract.Model{
        return model
    }

    @ActivityScope
    @Provides
    fun provideDatas() = mutableListOf<TestBean>()


    @ActivityScope
    @Provides
    fun provideTestAdapter(datas:MutableList<TestBean>) = TestAdapter(datas)

}