package com.daomingedu.onecpteacher.util

import android.content.Context

object SharedPreferencesUtil {

    private const val SHAREDPREFERENCESNAME = "CONFIG"

    private const val UUID = "UUID"
    private const val DEVICEID = "DEVICEID"
    private const val REGISTER = "REGISTER"
    private const val USER = "USER"
    private const val ISSHOWBUTTON = "ISSHOWBUTTON"
    private const val ISSHOWFOLDER = "ISSHOWFOLDER"
    private const val ISSHOWVIDEO = "ISSHOWVIDEO"
    private const val ISSHOWIMPORT = "ISSHOWIMPORT"
    private const val LIMIT = "LIMIT"
    private const val WATER = "WATER"

    fun setWater(context: Context, Water: Int){
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putInt(WATER, Water)
        edit.apply()
    }

    fun getWater(context: Context): Int{
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(WATER, 1)
    }


    fun setLimit(context: Context, Limit: Int){
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putInt(LIMIT, Limit)
        edit.apply()
    }

    fun getLimit(context: Context): Int{
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(LIMIT, 0)
    }

    fun setIsShowImport(context: Context, Import: Int){
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putInt(ISSHOWVIDEO, Import)
        edit.apply()
    }

    fun getIsShowImport(context: Context): Int{
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(ISSHOWIMPORT, 0)
    }

    fun setIsShowVideo(context: Context, isShowVideo: Int){
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putInt(ISSHOWVIDEO, isShowVideo)
        edit.apply()
    }

    fun getIsShowVideo(context: Context): Int{
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(ISSHOWVIDEO, 0)
    }


    fun setIsShowFolder(context: Context, isShowFolder: Int){
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putInt(ISSHOWFOLDER, isShowFolder)
        edit.apply()
    }

    fun getIsShowFolder(context: Context): Int{
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(ISSHOWFOLDER, 0)
    }

    fun setIsShowButton(context: Context, isShowButton: Int){
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putInt(ISSHOWBUTTON, isShowButton)
        edit.apply()
    }

    fun getIsShowButton(context: Context): Int{
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(ISSHOWBUTTON, 0)
    }

    fun setUUID(context: Context, uuid: String){
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putString(UUID, uuid)
        edit.apply()
    }

    fun getUUID(context: Context): String{
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(UUID, "")!!
    }

    fun setDeviceId(context: Context, deviceId: String){
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putString(DEVICEID, deviceId)
        edit.apply()
    }

    fun getDeviceId(context: Context): String{
        val sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(DEVICEID, "")!!
    }


}