package com.daomingedu.onecpteacher.app

import android.app.Activity
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.daomingedu.onecpteacher.R


import com.lzy.imagepicker.loader.ImageLoader
import java.io.File

class GlideImageLoader : ImageLoader {
    override fun displayImage(
      activity: Activity,
      path: String,
      imageView: ImageView,
      width: Int,
      height: Int
    ) {
        val requestOptions = RequestOptions()
            .error(R.drawable.ic_default_image)
            .placeholder(R.drawable.ic_default_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(activity)
            .load(Uri.fromFile(File(path)))
            .apply(requestOptions)
            .into(imageView)
    }

    override fun displayImagePreview(
      activity: Activity,
      path: String,
      imageView: ImageView,
      width: Int,
      height: Int
    ) {
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(activity)
            .load(Uri.fromFile(File(path)))
            .apply(requestOptions)
            .into(imageView)
    }


    override fun clearMemoryCache() {

    }
}