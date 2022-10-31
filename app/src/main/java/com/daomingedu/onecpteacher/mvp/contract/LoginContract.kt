package com.daomingedu.onecpteacher.mvp.contract

import com.daomingedu.onecpteacher.mvp.model.entity.BaseJson
import com.daomingedu.onecpteacher.mvp.model.entity.RegisterBean
import com.daomingedu.onecpteacher.mvp.model.entity.VersionBean
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
interface LoginContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun requestLoginSuccess();
        fun requestVersionInfoSuccess(data: VersionBean)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun login(
            key: String, mobile: String, password: String, versionCode: Int
        ): Observable<BaseJson<RegisterBean>>

        fun getVersionInfo(
            key: String
        ): Observable<BaseJson<VersionBean>>


    }

}