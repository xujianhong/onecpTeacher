package com.daomingedu.onecpteacher.mvp.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.di.component.DaggerGradeComponent
import com.daomingedu.onecpteacher.di.module.GradeModule
import com.daomingedu.onecpteacher.mvp.contract.GradeContract
import com.daomingedu.onecpteacher.mvp.model.entity.GradeBean
import com.daomingedu.onecpteacher.mvp.presenter.GradePresenter
import com.daomingedu.onecpteacher.mvp.ui.adapter.GradeAdapter
import com.daomingedu.onecpteacher.mvp.ui.wiget.LoadingDialog
import com.daomingedu.onecpteacher.util.SpeechUtils
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils
import kotlinx.android.synthetic.main.activity_grade.*
import javax.inject.Inject

class GradeActivity : BaseActivity<GradePresenter>(), GradeContract.View {


    companion object {
        const val CLASS_ID = "class_id"
    }

    lateinit var testId: String

    @Inject
    lateinit var mAdapter: GradeAdapter
    @Inject
    lateinit var mData: MutableList<GradeBean>


    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerGradeComponent.builder().appComponent(appComponent).gradeModule(GradeModule(this))
            .build().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_grade
    }

    override fun initData(savedInstanceState: Bundle?) {
        title = "年级列表"
        testId = intent.getStringExtra(TestActivity.TEST_ID)!!

        mPresenter?.getTest(testId)

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mAdapter.apply {

            setOnItemClickListener { adapter, view, position ->
                val intent = Intent(this@GradeActivity,StudentListActivity::class.java)
                intent.putExtra(CLASS_ID,mData[position].classId)
                intent.putExtra(TestActivity.TEST_ID,testId)
                startActivity(intent)
            }
        }
    }

    override fun requestGradeSuccess() {
        SpeechUtils.getInstance(this).speakText("请选择年级")
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