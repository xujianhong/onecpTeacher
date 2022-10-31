package com.daomingedu.onecpteacher.mvp.contract

import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.GradeBean
import com.daomingedu.onecpteacher.mvp.model.entity.TestBean
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
interface GradeContract {
    interface View : IView {
        fun requestGradeSuccess()
    }

    interface Model : IModel {
        fun getGrade(sessionId: String,testId:String): Observable<BaseJson<MutableList<GradeBean>>>
    }
}