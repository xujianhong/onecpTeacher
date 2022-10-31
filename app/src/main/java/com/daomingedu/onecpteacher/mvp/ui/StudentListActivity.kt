package com.daomingedu.onecpteacher.mvp.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.di.component.DaggerStudentListComponent
import com.daomingedu.onecpteacher.di.module.StudentListModule
import com.daomingedu.onecpteacher.mvp.contract.StudentListContract
import com.daomingedu.onecpteacher.mvp.model.entity.StudentListBean
import com.daomingedu.onecpteacher.mvp.presenter.StudentListPresenter
import com.daomingedu.onecpteacher.mvp.ui.adapter.StudentListAdapter
import com.daomingedu.onecpteacher.mvp.ui.wiget.LoadingDialog
import com.daomingedu.onecpteacher.util.SpeechUtils
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils
import kotlinx.android.synthetic.main.activity_student_list.*
import javax.inject.Inject

class StudentListActivity : BaseActivity<StudentListPresenter>(), StudentListContract.View {

    companion object {
        const val STUDENT_ID = "student_id"
        const val TESTSIGN_ID = "testSign_id"
        var currentIsUpLoad: Int = 0; //当前选择学生是否可以上传
    }

    lateinit var classId: String
    lateinit var testId: String

    @Inject
    lateinit var mAdapter: StudentListAdapter

    @Inject
    lateinit var mData: MutableList<StudentListBean>

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerStudentListComponent.builder().appComponent(appComponent)
            .studentListModule(StudentListModule(this)).build().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_student_list
    }

    override fun initData(savedInstanceState: Bundle?) {
        title = "学生列表"

        classId = intent.getStringExtra(GradeActivity.CLASS_ID)!!
        testId = intent.getStringExtra(TestActivity.TEST_ID)!!

        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mAdapter.apply {

            setOnItemClickListener { adapter, view, position ->
                mData[position].apply {
                    currentIsUpLoad = isUpload
                    val intent =
                        Intent(this@StudentListActivity, StudentTestInfoActivity::class.java)
                    intent.putExtra(TESTSIGN_ID, testSignId)
                    intent.putExtra(STUDENT_ID, studentId)
                    intent.putExtra(TestActivity.TEST_ID, testId)
                    startActivity(intent)
                }
            }
        }

        mPresenter?.getStudentList(true, testId, classId)

    }

    override fun onResume() {
        super.onResume()
        SpeechUtils.getInstance(this).speakText("请选择学生")
    }

    override fun requestStudentListSuccess() {

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