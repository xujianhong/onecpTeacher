package com.daomingedu.onecpteacher.util

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.HttpParams
import com.lzy.okgo.model.Response
import org.json.JSONObject
import timber.log.Timber
import java.io.File

object MyOkGoUtil {

    fun post(context: Context, url: String, params: HttpParams, handler: Handler) {
        OkGo.post<String>(url)
            .tag(this)
            .params(params)
            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                    Timber.d(Gson().toJson(response.body()))
                    when (response.isSuccessful) {
                        true -> {
                            val jsonObject = JSONObject(response.body())
                            val errno = jsonObject.optInt("code")
                            val msg = jsonObject.optString("message")
                            when (errno) {
                                0 -> {
                                    val data = jsonObject.optJSONObject("data")
                                    if (data != null) {
                                        handler.obtainMessage(0, data.toString()).sendToTarget()
                                    } else {
                                        handler.obtainMessage(0, "").sendToTarget()
                                    }
                                }
                                401 -> {

                                }
                                9 -> {
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    //handler.obtainMessage(0, "").sendToTarget()
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        false -> {
                            onError(response)
                        }
                    }
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    //handler.obtainMessage(0, "").sendToTarget()
                    Toast.makeText(context, response.exception.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }

    fun postnew(context: Context, url: String, params: HttpParams, handler: Handler) {
        OkGo.post<String>(url)
            .tag(this)
            .params(params)
            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                    Timber.d(Gson().toJson(response.body()))
                    when (response.isSuccessful) {
                        true -> {
                            val jsonObject = JSONObject(response.body())
                            val errno = jsonObject.optInt("result")
                            val msg = jsonObject.optString("msg")
                            when (errno) {
                                100 -> {
                                    val data = jsonObject.optJSONObject("data")
                                    if (data != null) {
                                        handler.obtainMessage(0, data.toString()).sendToTarget()
                                    } else {
                                        handler.obtainMessage(0, "").sendToTarget()
                                    }
                                }
                                9 -> {
                                    handler.obtainMessage(9, "").sendToTarget()
                                    //LoginActivity.startLoginActivity(context as AppCompatActivity)
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                                203 -> {
                                    handler.obtainMessage(203, "").sendToTarget()
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    handler.obtainMessage(errno, msg).sendToTarget()
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        false -> {
                            onError(response)
                        }
                    }
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    handler.obtainMessage(-1, "").sendToTarget()
                    Toast.makeText(context, response.exception.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }

    fun postGetString(context: Context, url: String, params: HttpParams, handler: Handler) {
        OkGo.post<String>(url)
            .tag(this)
            .params(params)
            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                    Timber.d(Gson().toJson(response.body()))
                    when (response.isSuccessful) {
                        true -> {
                            val jsonObject = JSONObject(response.body())
                            val errno = jsonObject.optInt("code")
                            val msg = jsonObject.optString("message")
                            when (errno) {
                                0 -> {
                                    val data = jsonObject.optString("data")
                                    if (data != null) {
                                        handler.obtainMessage(0, data.toString()).sendToTarget()
                                    } else {
                                        handler.obtainMessage(0, "").sendToTarget()
                                    }
                                }
                                401 -> {
                                    handler.obtainMessage(errno, "").sendToTarget()
                                }
                                9 -> {
                                    handler.obtainMessage(errno, "").sendToTarget()
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    handler.obtainMessage(errno, "").sendToTarget()
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        false -> {
                            onError(response)
                        }
                    }
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    handler.obtainMessage(-1, "").sendToTarget()
                }
            })
    }

    fun getJsonObjectData(context: Context, url: String, params: HttpParams, handler: Handler) {
        OkGo.get<String>(url)
            .params(params)
            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                    Log.d("test", Gson().toJson(response.body()))
                    when (response.isSuccessful) {
                        true -> {
                            val jsonObject = JSONObject(response.body())
                            val errno = jsonObject.optInt("result")
                            val msg = jsonObject.optString("msg")
                            when (errno) {
                                100 -> {
                                    val data = jsonObject.optJSONObject("data")
                                    if (data != null) {
                                        handler.obtainMessage(0, data.toString()).sendToTarget()
                                    } else {
                                        handler.obtainMessage(0, "").sendToTarget()
                                    }
                                }
                                9 -> {
                                    handler.obtainMessage(9, "").sendToTarget()
                                    //LoginActivity.startLoginActivity(context as AppCompatActivity)
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    handler.obtainMessage(errno, "").sendToTarget()
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        false -> {
                            onError(response)
                        }
                    }
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    handler.obtainMessage(-1, "").sendToTarget()
                    Toast.makeText(context, response.exception.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }

    fun postString(context: Context, url: String, params: HttpParams, handler: Handler) {
        OkGo.post<String>(url)
            .tag(this)
            .params(params)
            .execute(object : StringCallback() {
                override fun onSuccess(response: Response<String>) {
                    Timber.d(Gson().toJson(response.body()))
                    when (response.isSuccessful) {
                        true -> {
                            val jsonObject = JSONObject(response.body())
                            val errno = jsonObject.optInt("result")
                            val msg = jsonObject.optString("msg")
                            when (errno) {
                                100 -> {
                                    val data = jsonObject.optString("data")
                                    if (data != null) {
                                        handler.obtainMessage(0, data.toString()).sendToTarget()
                                    } else {
                                        handler.obtainMessage(0, "").sendToTarget()
                                    }
                                }
                                9 -> {
                                    handler.obtainMessage(9, "").sendToTarget()
                                    //LoginActivity.startLoginActivity(context as AppCompatActivity)
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    handler.obtainMessage(errno, "").sendToTarget()
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        false -> {
                            onError(response)
                        }
                    }
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    handler.obtainMessage(-1, "").sendToTarget()
                    Toast.makeText(context, response.exception.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }
}