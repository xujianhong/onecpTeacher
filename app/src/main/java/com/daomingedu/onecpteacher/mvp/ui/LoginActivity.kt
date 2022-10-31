package com.daomingedu.onecpteacher.mvp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.daomingedu.onecp.app.onClick
import com.daomingedu.onecpteacher.BuildConfig
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.app.Constant
import com.daomingedu.onecpteacher.app.Preference
import com.daomingedu.onecpteacher.di.component.DaggerLoginComponent
import com.daomingedu.onecpteacher.di.module.LoginModule
import com.daomingedu.onecpteacher.mvp.contract.LoginContract
import com.daomingedu.onecpteacher.mvp.model.entity.VersionBean
import com.daomingedu.onecpteacher.mvp.presenter.LoginPresenter
import com.daomingedu.onecpteacher.mvp.ui.wiget.LoadingDialog
import com.daomingedu.onecpteacher.util.SpeechUtils
import com.iflytek.cloud.*
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils
import constant.UiType
import kotlinx.android.synthetic.main.activity_login.*
import model.UiConfig
import model.UpdateConfig
import update.UpdateAppUtils

class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View{


    private var mobile by Preference(Constant.MOBILE, "")

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }



    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerLoginComponent.builder().appComponent(appComponent)
            .loginModule(LoginModule(this))
            .build().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_login
    }





    override fun initData(savedInstanceState: Bundle?) {
        etInputPhoneNumber.setText(mobile)
        btnLogin.onClick {


//            val intent = Intent()
//            intent.setType("video/*")
//            intent.setAction(Intent.ACTION_GET_CONTENT)
//            startActivityForResult(intent,1)
            val mobile = etInputPhoneNumber.text.toString().trim()
            if (TextUtils.isEmpty(mobile)) {
                ArmsUtils.makeText(application, "手机号不能为空")
                return@onClick
            }
            val pwd = etInputPwd.text.toString().trim()
            if (TextUtils.isEmpty(pwd)) {
                ArmsUtils.makeText(application, "密码不能为空")
                return@onClick
            }
            showLoading()
            mPresenter?.login(mobile, pwd)
        }

        mPresenter?.getVersionInfo()
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if(requestCode ==1){
//            if(resultCode == RESULT_OK){
//                val uri = data?.data
//                val cursor = uri?.let { contentResolver.query(it,null,null,null,null) }
//                if (cursor != null) {
//                    cursor.moveToFirst()
//                    Log.e(TAG,cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)).toString())
//                }
//
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }

    override fun requestLoginSuccess() {
        hideLoading()
        mobile = etInputPhoneNumber.text.toString().trim()
        startActivity(Intent(this@LoginActivity, TestActivity::class.java))
        finish()
    }

    override fun requestVersionInfoSuccess(data: VersionBean) {
        data.let {
            if (BuildConfig.VERSION_CODE < it.versionCode &&
                it.path.startsWith("https://")
            ) {

                // ui配置
                val uiConfig = UiConfig().apply {
                    uiType = UiType.PLENTIFUL
                    cancelBtnText = "下次再说"
//                    updateLogoImgRes = R.mipmap.ic_launcher
//                    updateBtnBgRes = R.mipmap.ic_launcher
//                    titleTextColor = Color.BLACK
//                    titleTextSize = 18f
//                    contentTextColor = Color.parseColor("#88e16531")
                }

                // 更新配置
                val updateConfig = UpdateConfig().apply {
                    force = it.isMust == 1
                    checkWifi = true
//                    needCheckMd5 = true
                    isShowNotification = true
                    alwaysShowDownLoadDialog = true

                    notifyImgRes = R.mipmap.ic_launcher
//                    apkSavePath = Environment.getExternalStorageDirectory().absolutePath +"/teprinciple"
//                    apkSaveName = "teprinciple"
                }
                UpdateAppUtils
                    .getInstance()
                    .apkUrl(it.path)
                    .updateTitle("发现新版本${it.versionName}")
                    .updateContent(it.remark)
                    .updateConfig(updateConfig)
//                    .uiConfig(uiConfig)
                    .update()
            }
        }
    }

    override fun showMessage(message: String) {
        ArmsUtils.snackbarText(message)
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()

        loadingDialog.dismiss()
    }

    override fun hideLoading() {
        loadingDialog.hide()
    }


}