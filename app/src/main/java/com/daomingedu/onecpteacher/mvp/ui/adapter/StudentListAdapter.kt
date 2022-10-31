package com.daomingedu.onecpteacher.mvp.ui.adapter

import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.mvp.model.entity.StudentListBean
import com.jess.arms.utils.ArmsUtils

/**
 * Description
 * Created by jianhongxu on 2021/10/21
 */
class StudentListAdapter(data: MutableList<StudentListBean>) :
    BaseQuickAdapter<StudentListBean, BaseViewHolder>(
        R.layout.item_student, data
    ) {
    override fun convert(helper: BaseViewHolder, item: StudentListBean) {
        helper.setText(R.id.tv_name,item.name)
//        helper.setText(R.id.tv_phone,"电话：${item.mobilePhone}")

        Glide.with(mContext).load(item.photoImg).into(helper.getView(R.id.imageView))

        when (item.isAudit) {
            2 -> {
                helper.getView<TextView>(R.id.tvState)
                    .setTextColor(ArmsUtils.getColor(mContext, R.color.red_c6))
                helper.setText(R.id.tvState, "视频未上传")
            }
            1 -> {
                helper.getView<TextView>(R.id.tvState)
                    .setTextColor(ArmsUtils.getColor(mContext, R.color.red_c6))
                helper.setText(R.id.tvState, "视频待审核")
            }
            0 -> {
                helper.getView<TextView>(R.id.tvState)
                    .setTextColor(ArmsUtils.getColor(mContext, R.color.blue_500))
                helper.setText(R.id.tvState, "上传成功")

            }

        }
    }
}