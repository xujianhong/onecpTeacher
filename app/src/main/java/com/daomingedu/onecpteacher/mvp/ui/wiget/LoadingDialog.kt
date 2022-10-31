package com.daomingedu.onecpteacher.mvp.ui.wiget

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import com.daomingedu.onecpteacher.R
import kotlinx.android.synthetic.main.dialog_loading_progress.*


/**
 * 加载进度条
 *
 *
 * Created by lcy on 15/7/21.
 * email:lcyzxin@gmail.com
 * version 1.0
 */
class LoadingDialog(context: Context, theme: Int = R.style.LoadingDialog, tips: String = "加载中...") :
    Dialog(context, theme) {

    init {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_loading_progress, null)
        setContentView(contentView)
        //gradle 添加 id 'kotlin-android-extensions' //可以直接使用layout id 名称获取当前view对象
        tvLoadingTips.text = tips

        val window = window
        if (window != null) {
            window.attributes.gravity = Gravity.CENTER
            window.attributes.alpha = 0.9f
        }
        setCanceledOnTouchOutside(false)
    }
}
