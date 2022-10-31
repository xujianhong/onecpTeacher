package com.daomingedu.onecpteacher.mvp.contract

import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.GradeBean
import com.daomingedu.onecpteacher.mvp.model.entity.StudentListBean
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
interface StudentListContract {
    interface View : IView {
        fun requestStudentListSuccess()
    }

    interface Model : IModel {
        fun getStudentList(
            sessionId: String,
            testId: String,
            classId: String,
            start: Int,
            size: Int
        ): Observable<BaseJson<MutableList<StudentListBean>>>
    }
}