/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.daomingedu.onecpteacher.mvp.model.api

/**
 * ================================================
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 *
 *
 * Created by JessYan on 08/05/2016 11:25
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
interface Api {
    companion object {

//        const val APP_DOMAIN = "http://www.daomingedu.com/"
        const val APP_DOMAIN = "http://onecp.4hand.com.cn/onecp/"
//        const val APP_DOMAIN = "http://114.117.194.109/"


        const val SUCCESS = 0

        //客服跳转地址
        const val CUSTOMER_SERVICE =
            "https://h5.youzan.com/v3/im/index?c=wsc&v=2&o=https%3A%2F%2Fshop40750931.youzan.com&kdt_id=40558763&type=goods&alias=&source_key=1593077657150&target=%2F#/index"


        val LOGIN_TIME_OUT = 1001
        val BUSINESS_ERROR = 203
        val SYSTEM_ERROR = 202
        val REQUEST_PARAM_ERROR = 201
    }
}
