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
package com.daomingedu.onecpteacher.mvp.model.api.service



import com.daomingedu.onecpteacher.mvp.model.entity.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * ================================================
 * 展示 [Retrofit.create] 中需要传入的 ApiService 的使用方式
 * 存放关于用户的一些 API
 *
 *
 * Created by JessYan on 08/05/2016 12:05
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
interface CustomerService {
    /*String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    @Headers({HEADER_API_VERSION})
    @GET("/users")
    Observable<List<Object>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage);*/



    /**
     * 登录
     *
     * @param mobile 登录手机号	true
     * @param password  登录密码	true
     * @param versionCode   登录APP版本号	true
     * @return
     */
    @FormUrlEncoded
    @POST("api/teacher/login")
    fun login(
        @Field("key") key: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("versionCode") versionCode: Int
    ): Observable<BaseJson<RegisterBean>>



    @FormUrlEncoded
    @POST("api/teacher/getTests")
    fun getTests(
        @Field("sessionId")sessionId:String
    ):Observable<BaseJson<MutableList<TestBean>>>


    @FormUrlEncoded
    @POST("api/teacher/getClassList")
    fun getGrade(
        @Field("sessionId")sessionId:String,
        @Field("testId")testId:String
    ):Observable<BaseJson<MutableList<GradeBean>>>

    @FormUrlEncoded
    @POST("api/teacher/getStudentList")
    fun getStudentList(
        @Field("sessionId")sessionId:String,
        @Field("testId")testId:String,
        @Field("classId")classId:String,
        @Field("start")start:Int,
        @Field("size")size:Int
    ):Observable<BaseJson<MutableList<StudentListBean>>>


    @FormUrlEncoded
    @POST("api/teacher/getStudentTestInfo")
    fun getStudentTestInfo(
        @Field("sessionId")sessionId:String,
        @Field("testId")testId:String,
        @Field("studentId")studentId:String,
        @Field("testSignId")testSignId:String
    ):Observable<BaseJson<StudentTestInfoBean>>


    /**
     * 获取人脸识别
     */
    @FormUrlEncoded
    @POST("api/teacher/getCompareFaceScore.do")
    fun getCompareFaceScore(
        @Field("sessionId")sessionId:String,
        @Field("testSignId")testSignId:String,
        @Field("base64Image")base64Image:String,
    ):Observable<BaseJson<CompareFaceScoreBean>>


    /**
     * 获取视频上传前说需要的参数
     */
    @FormUrlEncoded
    @POST("api/teacher/getUploadVideoParam.do")
    fun getUploadVideoParam(
        @Field("sessionId")sessionId:String,
        @Field("testSignId")testSignId:String
    ):Observable<BaseJson<UploadVideoParamBean>>


    /**
     * 上传腾讯云后保存视频
     */
    @FormUrlEncoded
    @POST("api/teacher/saveUploadVideo.do")
    fun saveUploadVideo(
        @Field("sessionId")sessionId:String,
        @Field("testSignId")testSignId:String,
        @Field("videoPath")videoPath:String,
        @Field("videoSize")videoSize:String,
        @Field("isAudit")isAudit:Int,
        @Field("catalogId")catalogId:String
    ):Observable<BaseJson<Any>>



    /**
     * 最新版本信息
     *
     * @param key   key=bfbcd9d2974b4d33bca9012b4b2b28c5
     * @param systemType    1:android 2:IOS
     * @return
     */
    @FormUrlEncoded
    @POST("api/teacher/getVersionInfo")
    fun getVersionInfo(
        @Field("key") key: String,
    ): Observable<BaseJson<VersionBean>>
}
