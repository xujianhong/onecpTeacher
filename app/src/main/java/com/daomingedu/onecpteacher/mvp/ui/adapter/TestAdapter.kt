package com.daomingedu.onecpteacher.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.mvp.model.entity.TestBean

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
class TestAdapter(datas: MutableList<TestBean>) :
    BaseQuickAdapter<TestBean, BaseViewHolder>(
        R.layout.item_test, datas
    ) {
    override fun convert(helper: BaseViewHolder, item: TestBean) {
        helper.setText(R.id.tvEnrollTime, "${item.startSignDate}-${item.endSignDate}")
        helper.setText(R.id.tvTestName,item.testName)
        when(item.testType){
            1->{
                helper.setText(R.id.tvTestContent,"视频")
            }
            2->{
                helper.setText(R.id.tvTestContent,"图片")
            }
        }

    }
}