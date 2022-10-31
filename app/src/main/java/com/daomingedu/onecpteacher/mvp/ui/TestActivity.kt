package com.daomingedu.onecpteacher.mvp.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.di.component.DaggerTestComponent
import com.daomingedu.onecpteacher.di.module.TestModule
import com.daomingedu.onecpteacher.mvp.contract.TestContract
import com.daomingedu.onecpteacher.mvp.model.entity.TestBean
import com.daomingedu.onecpteacher.mvp.presenter.TestPresenter
import com.daomingedu.onecpteacher.mvp.ui.adapter.TestAdapter
import com.daomingedu.onecpteacher.mvp.ui.wiget.LoadingDialog
import com.daomingedu.onecpteacher.util.SpeechUtils
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils
import kotlinx.android.synthetic.main.activity_test.*
import javax.inject.Inject

class TestActivity : BaseActivity<TestPresenter>(), TestContract.View {

    @Inject
    lateinit var mAdapter: TestAdapter

    @Inject
    lateinit var mData: MutableList<TestBean>


    companion object{
        const val TEST_ID="test_id"
    }

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerTestComponent.builder().appComponent(appComponent).testModule(TestModule(this))
            .build().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_test
    }

    override fun initData(savedInstanceState: Bundle?) {
        mPresenter?.getTest()
        recyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        recyclerView.adapter =mAdapter.apply {

            setOnItemClickListener { adapter, view, position ->
                val intent = Intent(this@TestActivity,GradeActivity::class.java)
                intent.putExtra(TEST_ID,mData[position].id)
                startActivity(intent)
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            mPresenter?.getTest()
        }
    }

    override fun onResume() {
        super.onResume()
        SpeechUtils.getInstance(this).speakText("请选择需要测评的项")
    }





    override fun requestTestListSuccess() {
        swipeRefreshLayout.isRefreshing = false
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