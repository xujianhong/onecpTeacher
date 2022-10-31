package com.daomingedu.onecpteacher.mvp.ui.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.mvp.model.entity.GradeBean

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
class GradeAdapter(data: MutableList<GradeBean>) :
    BaseQuickAdapter<GradeBean, BaseViewHolder>(R.layout.item_grade, data) {
    override fun convert(helper: BaseViewHolder, item: GradeBean) {
        helper.setText(R.id.tv_school, item.schoolName)
        helper.setText(R.id.tv_grade, item.gradeName+item.classesName)

    }
}