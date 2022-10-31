package com.daomingedu.onecpteacher.mvp.ui

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.daomingedu.onecp.app.onClick
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.app.Constant
import com.daomingedu.onecpteacher.app.Preference
import com.daomingedu.onecpteacher.di.component.DaggerUpdateVideoComponent
import com.daomingedu.onecpteacher.di.module.UpdateVideoModule
import com.daomingedu.onecpteacher.mvp.contract.UpdateVideoContract
import com.daomingedu.onecpteacher.mvp.model.entity.CompareFaceScoreBean
import com.daomingedu.onecpteacher.mvp.model.entity.LocalWork
import com.daomingedu.onecpteacher.mvp.model.entity.UploadVideoParamBean
import com.daomingedu.onecpteacher.mvp.presenter.UpdateVideoPresenter
import com.daomingedu.onecpteacher.util.MediaController
import com.daomingedu.onecpteacher.util.MemoryUtil
import com.daomingedu.onecpteacher.util.MySessionCredentialProvider
import com.daomingedu.onecpteacher.util.SpeechUtils
import com.google.gson.Gson
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils
import com.jmolsmobile.landscapevideocapture.VideoCaptureActivity
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration
import com.jmolsmobile.landscapevideocapture.configuration.PredefinedCaptureConfigurations
import com.pili.pldroid.player.PLOnCompletionListener
import com.pili.pldroid.player.PLOnErrorListener
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.CosXmlSimpleService
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlProgressListener
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import kotlinx.android.synthetic.main.activity_upload_video.*
import kotlinx.android.synthetic.main.activity_upload_video.PLVideoTextureView
import kotlinx.android.synthetic.main.activity_upload_video_play.*
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class UploadVideoActivity : BaseActivity<UpdateVideoPresenter>(), UpdateVideoContract.View {

    private var videoTime by Preference(Constant.VIDEO_TIME, 0)
    private var videoPixel by Preference(Constant.VIDEO_PIXEL, 0)

    lateinit var testSignId: String

    //上传的文件
    var selectVideoFile: File? = null

    lateinit var compareFaceScoreBean: CompareFaceScoreBean

    var uploadVideoParamBean: UploadVideoParamBean? = null

    var progressDialog: ProgressDialog? = null


    lateinit var ErrorMsg: String //获取视频上传参数错误信息


    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerUpdateVideoComponent.builder().appComponent(appComponent).updateVideoModule(
            UpdateVideoModule(this)
        ).build().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_upload_video
    }

    override fun initData(savedInstanceState: Bundle?) {
        testSignId = intent.getStringExtra(StudentListActivity.TESTSIGN_ID)!!
        title = "上传视频"

        btnLocalFile.onClick { //选择本地文件

            UploadVideoListAct.startUploadVideoListActivity(
                this,
                UploadVideoListAct.TYPE_RETURN_YES,
                UploadVideoListAct.IMPORT_YES
            )
        }

        ivClose.onClick { //删除已选择的视频文件
//            if (selectVideoFile != null && selectVideoFile!!.exists())
//                selectVideoFile?.delete()
//            else
            PLVideoTextureView.stopPlayback()
            selectVideoFile = null
            rlVideo.visibility = View.GONE
            tvScore.text = ""
        }

        btnTakeCamera.onClick {//拍摄视频
            RxPermissions(this).request(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
                .subscribe {
                    if (it) {
                        when (MemoryUtil.memoryIsAvailble(this)) {
                            MemoryUtil.MEMORY_OK -> takeVideo("")
                            MemoryUtil.MEMORY_OUT -> {
                                val dialog = AlertDialog.Builder(this)
                                dialog.setMessage("存储空间过小, 可能会出现录制完了无法保存视频的情况, 是否继续拍摄视频")
                                dialog.setCancelable(false)
                                dialog.setPositiveButton("继续") { dialog, which ->
                                    dialog?.dismiss()
                                    takeVideo("")
                                }
                                dialog.setNegativeButton("取消") { dialog, which -> dialog?.dismiss() }
                                dialog.create().show()
                            }

                        }
                    } else {
                        showMessage("请开启相关权限")
                    }
                }
        }

        btnUpdate.onClick {
            var catalogId:String? = null
            uploadVideoParamBean?.textbook?.forEach {
                if(it.name == spSelectCatalog?.tag?.toString()){
                    it.catalogList.forEach { catalog ->
                        var currentCatalog = spSelectCatalog.selectedItem.toString()
                        if(currentCatalog == catalog.name){
                            catalogId =catalog.id
                            return@forEach
                        }
                    }
                    return@forEach
                }
            }

            if(catalogId==null){
                showMessage("请选择教材和目录")
                return@onClick
            }

            if (uploadVideoParamBean == null) {
                showMessage(ErrorMsg)
                Timber.e("腾讯云所需数据为null")
                return@onClick
            }
            if (selectVideoFile == null || !selectVideoFile!!.exists()) {
                showMessage("选择视频或拍摄视频")
                return@onClick
            }
            if (compareFaceScoreBean.isUpload == 0) {
                showMessage("人脸识别未通过")
                return@onClick
            }

            updateCoS(selectVideoFile!!, uploadVideoParamBean!!,catalogId!!)

        }

        mPresenter?.getUploadVideoParam(testSignId)
        PLVideoTextureView.setMediaController(MediaController(this))
    }

    /**
     * 上传腾讯云
     */
    private fun updateCoS(file: File, bean: UploadVideoParamBean, catalogId: String) {

        val credentials = bean.cosTempKey.credentials
        val mySessionCredentialProvider = MySessionCredentialProvider(
            credentials.tmpSecretId,
            credentials.tmpSecretKey,
            credentials.sessionToken,
            bean.cosTempKey.startTime.toLong(),
            bean.cosTempKey.expiredTime.toLong()
        )
        val serviceConfig = CosXmlServiceConfig.Builder()
            .setRegion(bean.region)
            .builder()
        val cosXmlService = CosXmlSimpleService(this, serviceConfig, mySessionCredentialProvider)
        val transferConfig = TransferConfig.Builder().build()
        val transferManager = TransferManager(
            cosXmlService,
            transferConfig
        )

        val bucket = bean.bucketName  //存储桶，格式：BucketName-APPID
        val cosPath = "/${bean.uploadKey}mp4" //对象在存储桶中的位置标识符，即称对象键
        val srcPath = file.absolutePath
        val uploadId: String? = null

        progressDialog = ProgressDialog(this)
        progressDialog?.setCancelable(false)
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog?.setMessage("视频上传中")
        progressDialog?.show()
        // 上传文件
        val cosxmlUploadTask = transferManager.upload(
            bucket, cosPath,
            srcPath, uploadId
        )
        cosxmlUploadTask.setCosXmlProgressListener(object : CosXmlProgressListener {
            override fun onProgress(complete: Long, target: Long) {
                Log.d("test", "onProgress")
                Log.d("test", complete.toString())
                Log.d("test", target.toString())
                progressDialog?.max = 100
                progressDialog?.progress =
                    (((complete.toDouble() / target.toDouble()) * 100).toInt())
            }
        })
        cosxmlUploadTask.setCosXmlResultListener(object : CosXmlResultListener {
            override fun onSuccess(request: CosXmlRequest?, result: CosXmlResult?) {
                Log.d("test", "onSuccess")
                Log.d("test", Gson().toJson(result))
                progressDialog?.dismiss()
                //isUpToTencent = true
                //腾讯云上传成功后

                mPresenter?.saveUploadVideo(
                    testSignId,
                    "${uploadVideoParamBean!!.uploadKey}mp4",
                    selectVideoFile?.length().toString(),
                    compareFaceScoreBean.isAudit,
                    catalogId
                )
//                handler.sendEmptyMessage(1)
            }

            override fun onFail(
                request: CosXmlRequest?,
                exception: CosXmlClientException?,
                serviceException: CosXmlServiceException?
            ) {
                Log.d("test", "onFail")
                Log.d("test", exception?.errorCode.toString())
                Log.d("test", serviceException?.message)
                progressDialog?.dismiss()
            }

        })
    }

    /**
     * 打开摄像头录像
     */
    private fun takeVideo(fileName: String) {
        val config = createCaptureConfiguration()
        val intent = Intent(this, VideoCaptureActivity::class.java)
        intent.putExtra(VideoCaptureActivity.EXTRA_CAPTURE_CONFIGURATION, config)
        intent.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME, fileName) //文件名称

//        intent.putExtra(VideoCaptureActivity.WATER, SharedPreferencesUtil.getWater(this))
        intent.putExtra(VideoCaptureActivity.WATER, 0)//视频水印
        intent.putExtra("videoType", 1)
        startActivityForResult(intent, LocalWork.VIDEO)
    }

    /**
     * 录制视频配置
     */
    private fun createCaptureConfiguration(): CaptureConfiguration {
        //分辨率
        val resolution = when (2) {//videoPixel
            1 -> PredefinedCaptureConfigurations.CaptureResolution.RES_480P
            2 -> PredefinedCaptureConfigurations.CaptureResolution.RES_720P
            3 -> PredefinedCaptureConfigurations.CaptureResolution.RES_1080P
            else -> PredefinedCaptureConfigurations.CaptureResolution.RES_480P
        }
        //质量
        val quality = PredefinedCaptureConfigurations.CaptureQuality.LOW //低质量
        //限制时长
//        val fileDuration = videoTime * 60 //10分钟
        val fileDuration = videoTime
        //限制大小
        val filesize = CaptureConfiguration.NO_FILESIZE_LIMIT //不限
        //true显示录制视频时的时间，false为不显示
        val builder =
            CaptureConfiguration.Builder(resolution, quality)
        builder.maxDuration(fileDuration)
//        builder.maxFileSize(((application as MyApp).getVideo() / 1024 / 1024) as Int)
        builder.frameRate(PredefinedCaptureConfigurations.FPS_20)
//        builder.noCameraToggle() //关闭前置摄像头
        builder.showRecordingTime()
        return builder.build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        var videoPath: String? = null
        when (requestCode) {
            UploadVideoListAct.REQUEST_CODE -> {
                videoPath = data?.extras?.getString("result")!!
            }

            LocalWork.VIDEO -> {
                videoPath = data!!.getStringExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME)

            }

        }
        if (TextUtils.isEmpty(videoPath)) return
        if (selectVideoFile != null && selectVideoFile!!.exists()) {
            selectVideoFile?.delete()
        }
        selectVideoFile = File(videoPath)
        Timber.e("获得路径：   " + selectVideoFile?.absolutePath)
        initVideoPlay(selectVideoFile!!.absolutePath)
    }


    /**
     * 初始化视频播放
     */
    private fun initVideoPlay(videoPath: String) {

        loadVideoScreenshot(videoPath)
        rlVideo.visibility = View.VISIBLE

        PLVideoTextureView.setMediaController(MediaController(this))
        PLVideoTextureView.setVideoPath(videoPath)
        PLVideoTextureView.start()
        PLVideoTextureView.setOnErrorListener(object : PLOnErrorListener {
            override fun onError(p0: Int): Boolean {
                return false
            }
        })
        PLVideoTextureView.setOnCompletionListener(object : PLOnCompletionListener {
            override fun onCompletion() {
            }
        })
    }

    /**
     * 获取视频截屏
     */
    fun loadVideoScreenshot(videoUrl: String) {
        Glide.with(this)
            .setDefaultRequestOptions(
                RequestOptions()
                    .frame(3000000L)
            ).asBitmap()
            .load(videoUrl).addListener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
//                    ivVideo.setImageBitmap(resource)
                    val base64 = bitmapToBase64(resource!!)
                    mPresenter?.getCompareFaceScore(testSignId, base64)
                    return false
                }

            }).submit()
//            .into(ivVideo)

    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        var result = ""
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                baos.flush()
                baos.close()
                val bitmapBytes: ByteArray = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }

    override fun requestCompareFaceScoreSuccess(bean: CompareFaceScoreBean) {
        compareFaceScoreBean = bean
        tvScore.text = bean.text

        SpeechUtils.getInstance(this).speakText(bean.text)
    }

    override fun requestUploadVideoParam(bean: UploadVideoParamBean) {
        uploadVideoParamBean = bean
        tvContext2.text = bean.remark


        val mBookAdapter = ArrayAdapter<String>(this, R.layout.item_text, R.id.tvSelect)
        val mCatalogAdapter = ArrayAdapter<String>(this, R.layout.item_text, R.id.tvSelect)
        mBookAdapter.add("请选择教材")


        bean.textbook.forEach {
            mBookAdapter.add(it.name)
        }
        spSelectBook.adapter = mBookAdapter
        spSelectBook.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Timber.d(parent?.getItemAtPosition(position).toString())

                mCatalogAdapter.clear()
                when (position) {
                    0 -> return
                    else -> {
                        mCatalogAdapter.add("请选择目录")
                        bean.textbook.forEach {
                            if(it.name == parent?.getItemAtPosition(position).toString()){
                                spSelectCatalog.tag = it.name

                                it.catalogList.forEach { catalog ->
                                    mCatalogAdapter.add(catalog.name)
                                    return@forEach
                                }
                                return@forEach
                            }
                        }

                        spSelectCatalog.adapter = mCatalogAdapter
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        SpeechUtils.getInstance(this).speakText("请选择教材，再选择视频,然后再提交")
    }

    override fun requestUploadVideoParamError(msg: String) {
        ErrorMsg = msg
    }

    override fun requestSaveUploadVideo() {
        finish();
    }

    override fun showMessage(message: String) {
        ArmsUtils.snackbarText(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        SpeechUtils.getInstance(this).destroy()
    }
}