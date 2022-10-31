package com.daomingedu.onecpteacher.mvp.contract

import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.TestBean
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */

interface TestContract {
    interface View : IView {
        fun requestTestListSuccess()
    }

    interface Model : IModel {
        fun getTest(sessionId: String): Observable<BaseJson<MutableList<TestBean>>>
    }

}