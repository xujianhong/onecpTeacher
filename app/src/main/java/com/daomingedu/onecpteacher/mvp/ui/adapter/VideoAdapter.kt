package com.daomingedu.onecpteacher.mvp.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.daomingedu.onecpteacher.R
import com.daomingedu.onecpteacher.mvp.ui.UploadVideoPlayAct
import com.daomingedu.onecpteacher.util.Utils
import kotlinx.android.synthetic.main.item_videolist.view.*

import timber.log.Timber
import java.io.File

/**
 * 数据为空适配器
 */
class VideoAdapter(val context: Context, val list: ArrayList<File>, val type: Int) : RecyclerView.Adapter<VideoAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_videolist, p0, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.bindData(context, list[p1], p0.adapterPosition)
    }



    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bindData(context: Context, file: File, position: Int){
            when{
                file.name.endsWith(".mp4") -> Glide.with(context).load(Uri.fromFile(file)).into(
                    (view.item_videolist_iv)
                )
            }
            view.item_videolist_delete.setOnClickListener {
                val dialog = AlertDialog.Builder(context)
                dialog.setMessage("确定删除视频?")
                dialog.setPositiveButton("确定", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        list[position].delete()
                        list.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(0, list.size)
                        dialog?.dismiss()
                    }
                })
                dialog.setNegativeButton("取消", object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog?.dismiss()
                    }
                })
                dialog.create().show()
            }



            view.tvCreateTime.text = "拍摄于 "+ Utils.getTime(file.lastModified(),"yyyy-MM-dd HH:mm")
            view.item_videolist_scan.setOnClickListener {
                UploadVideoPlayAct.startUploadVideoPlayActivity(context as AppCompatActivity, file.absolutePath)
            }
            view.setOnClickListener {
                when(type){
                    1 -> {
                        when{
                            file.name.endsWith(".mp4") -> {
                                val intent = Intent()
                                intent.putExtra("result", file.absolutePath)
                                (context as AppCompatActivity).setResult(Activity.RESULT_OK, intent)
                                context.finish()
                            }
                        }
                    }
                }
            }
        }

    }
}