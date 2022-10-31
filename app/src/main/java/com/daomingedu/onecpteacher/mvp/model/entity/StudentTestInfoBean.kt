package com.daomingedu.onecpteacher.mvp.model.entity

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
class StudentTestInfoBean(
    val testSignId:String,
    val province:String,
    val provinceName:String,
    val cityName:String,
    val countyName:String,
    val schoolName:String,
    val gradeName:String,
    val classesName:String,
    val startUploadDate:String,
    val endUploadDate:String,
    val attendState:String,
    val attendScore:String,
    val attendComment:String,
    val student:StudentInfo
)

class StudentInfo(
    val mobilePhone:String,
    val name:String,
    val sex:String,
    val birthDay:String,
    val identityType:Int,
    val identityCard:String,
    val photoImg:String,
    val photoImgCheck:Int,
    val reason:String
)