package com.daomingedu.onecpteacher.mvp.contract

import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.StudentListBean
import com.daomingedu.onecpteacher.mvp.model.entity.StudentTestInfoBean
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
interface StudentTestInfoContract {

    interface View : IView {
        fun requestStudentTestSuccess(data:StudentTestInfoBean)
    }

    interface Model : IModel {
        fun getStudentTestInfo(
            sessionId: String,
            testId: String,
            studentId: String,
            testSignId: String
        ): Observable<BaseJson<StudentTestInfoBean>>
    }
}