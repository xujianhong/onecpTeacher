package com.daomingedu.onecpteacher.util

import android.content.Context
import android.content.DialogInterface
import android.os.Environment
import android.os.StatFs
import androidx.appcompat.app.AlertDialog

object MemoryUtil {

    const val MEMORY_OK =0  //内存容量满足
    const val MEMORY_OUT =1 //内存容量不满足

    fun getAvailableSize(): Long{
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return blockSize * availableBlocks
    }


    fun memoryIsAvailble(context: Context): Int{
        val size = getAvailableSize()
        val kb = 1024L
        val mb = 1024 * kb
        val gb = 1024 * mb
        if(size < mb * 25){
            val dialog = AlertDialog.Builder(context)
            dialog.setMessage("存储空间不足, 不能拍摄视频")
            dialog.setCancelable(false)
            dialog.setPositiveButton("知道了", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
            dialog.create().show()
            return MEMORY_OUT
        }else if (size < mb * 50){
            return MEMORY_OUT
        }
        return MEMORY_OK
    }
}