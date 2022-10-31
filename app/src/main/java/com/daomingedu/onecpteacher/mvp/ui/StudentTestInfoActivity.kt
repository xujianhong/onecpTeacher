package com.daomingedu.onecpteacher.mvp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.daomingedu.onecp.app.visible
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.di.component.DaggerStudentTestInfoComponent
import com.daomingedu.onecpteacher.di.module.StudentTestInfoModule
import com.daomingedu.onecpteacher.mvp.contract.StudentTestInfoContract
import com.daomingedu.onecpteacher.mvp.model.entity.StudentTestInfoBean
import com.daomingedu.onecpteacher.mvp.presenter.StudentTestInfoPresenter
import com.daomingedu.onecpteacher.mvp.ui.wiget.LoadingDialog
import com.daomingedu.onecpteacher.util.SpeechUtils
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils
import kotlinx.android.synthetic.main.activity_student_test_info.*

class StudentTestInfoActivity : BaseActivity<StudentTestInfoPresenter>(),
    StudentTestInfoContract.View {

    lateinit var testId: String
    lateinit var studentId: String
    lateinit var testSignId: String

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerStudentTestInfoComponent.builder().appComponent(appComponent).studentTestInfoModule(
            StudentTestInfoModule(this)
        ).build().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_student_test_info
    }

    override fun initData(savedInstanceState: Bundle?) {
        testId = intent.getStringExtra(TestActivity.TEST_ID)!!
        studentId = intent.getStringExtra(StudentListActivity.STUDENT_ID)!!
        testSignId = intent.getStringExtra(StudentListActivity.TESTSIGN_ID)!!

        title = "学生详情"
//        Log.e(this.TAG, "当前选择学生上传：${StudentListActivity.currentIsUpLoad}")
        when (StudentListActivity.currentIsUpLoad) {
            0 -> {
                btn_update.visible(false)
            }
            1 -> {
                btn_update.visible(true)
                btn_update.setOnClickListener {
                    val intent = Intent(this@StudentTestInfoActivity,UploadVideoActivity::class.java)
                    intent.putExtra(StudentListActivity.TESTSIGN_ID,testSignId)
                    startActivity(intent)
                }
            }
        }



        mPresenter?.getStudentTestInfo(testId, studentId, testSignId)
    }


    @SuppressLint("SetTextI18n")
    override fun requestStudentTestSuccess(data: StudentTestInfoBean) {
        data.apply {
            Glide.with(this@StudentTestInfoActivity).load(student.photoImg).into(ivAvatar)

            when (student.photoImgCheck) {
                0 -> {
                    tvCheck.setTextColor(
                        ArmsUtils.getColor(
                            this@StudentTestInfoActivity,
                            R.color.black_39
                        )
                    )
                    tvCheck.text = "待审核中"
                }
                1 -> {
                    tvCheck.setTextColor(
                        ArmsUtils.getColor(
                            this@StudentTestInfoActivity,
                            R.color.blue_500
                        )
                    )
                    tvCheck.text = "审核通过"
                }
                2 -> {
                    tvCheck.setTextColor(
                        ArmsUtils.getColor(
                            this@StudentTestInfoActivity,
                            R.color.red_c6
                        )
                    )
                    tvCheck.text = "审核未通过"

                    tvCheckReason.text = student.reason
                }
            }

            tv_name.text = "姓名：${student.name}"
            tv_phone.text = "电话：${student.mobilePhone}"
            if (student.sex == "M") {
                tv_sex.text = "性别：男"
            } else if (student.sex == "F") {
                tv_sex.text = "性别：女"
            }
            tv_birth.text = "生日：${student.birthDay}"
            when (student.identityType) {
                1 -> {
                    tv_credential_type.text = "证件类型：身份证"
                }
                2 -> {
                    tv_credential_type.text = "证件类型：港澳通行证"
                }
                3 -> {
                    tv_credential_type.text = "证件类型：台胞证"
                }
                4 -> {
                    tv_credential_type.text = "证件类型：护照"
                }
            }

            tv_credential_number.text = "证件号码：${student.identityCard}"

            tv_province.text = "省市区：${provinceName}${cityName}${countyName}"
            tv_school.text = "学校：${schoolName}"
            tv_grade.text = "年级：${gradeName}"
            tv_class.text = "班级：${classesName}"

            if (startUploadDate.isNotEmpty() && endUploadDate.isNotEmpty()) {
                tv_update.text = "上传日期: $startUploadDate 至 $endUploadDate"
            }


        }

        SpeechUtils.getInstance(this).speakText("请仔细核对学生信息")
    }

    override fun showMessage(message: String) {
        ArmsUtils.snackbarText(message)
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        SpeechUtils.getInstance(this).destroy()
        loadingDialog.dismiss()
    }

    override fun hideLoading() {
        loadingDialog.hide()
    }
}