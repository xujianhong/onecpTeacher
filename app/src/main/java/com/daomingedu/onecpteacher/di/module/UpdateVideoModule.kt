package com.daomingedu.onecpteacher.di.module


import com.daomingedu.onecpteacher.mvp.contract.UpdateVideoContract
import com.daomingedu.onecpteacher.mvp.model.UpdateVideoModel
import com.jess.arms.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by jianhongxu on 3/18/21
 * Description
 */
@Module
class UpdateVideoModule (private val view: UpdateVideoContract.View){
    @ActivityScope
    @Provides
    fun provideUpdateVideoView():UpdateVideoContract.View{
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideUpdateVideoModel(model: UpdateVideoModel):UpdateVideoContract.Model{
        return model
    }
}