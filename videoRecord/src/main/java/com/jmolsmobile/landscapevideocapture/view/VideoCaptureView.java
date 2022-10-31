/*
 *  Copyright 2016 Jeroen Mols
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jmolsmobile.landscapevideocapture.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jmolsmobile.landscapevideocapture.R;
import com.jmolsmobile.landscapevideocapture.preview.CapturePreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VideoCaptureView extends FrameLayout implements OnClickListener {

    private ImageView mDeclineBtnIv;
    private ImageView mAcceptBtnIv;
    private ImageView mRecordBtnIv;
    private ImageView mChangeCameraIv;
    private TextView videocapture_cancel;
    private TextView tv_tip_bottom;
    private TextView tv_tip_top;
    private ImageView iv_person;

    private SurfaceView mSurfaceView;
    private ImageView mThumbnailIv;
    private TextView mTimerTv;
    private Handler customHandler = new Handler();
    private long startTime = 0L;

    private RecordingButtonInterface mRecordingInterface;
    private boolean mShowTimer;
    private boolean isFrontCameraEnabled;
    private boolean isCameraSwitchingEnabled;
    private int orientationDegree;
    private Bitmap videoThumbnail;
    private TextView mTimerTv2;
    private boolean showTimer2;
    private Activity activity;

    public void rotateViews(int degree) {
        Log.e("cdcdcd","角度="+degree);
        orientationDegree = degree;
        switch (degree) {
            case 0:
            case -180:
                showTimer2 = false;
                break;
            case -90:
            case -270:
                showTimer2 = true;
                break;
        }
        mTimerTv.setRotation(degree);
        mDeclineBtnIv.setRotation(degree);
        mAcceptBtnIv.setRotation(degree);
        mChangeCameraIv.setRotation(degree);
        updateVideoThumbnail();
    }

    private void updateVideoThumbnail() {
        if (videoThumbnail != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(orientationDegree);
            Bitmap changedVideoThumbnail = Bitmap.createBitmap(videoThumbnail, 0, 0, videoThumbnail.getWidth(), videoThumbnail.getHeight(), matrix, true);
            mThumbnailIv.setImageBitmap(changedVideoThumbnail);
        }
    }

    public VideoCaptureView(Context context) {
        super(context);
        initialize(context);
    }

    public VideoCaptureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
        activity = (Activity) context;
    }

    public VideoCaptureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public static boolean saveBitmapToSdCard(Context context, Bitmap mybitmap, String name){
        boolean result = false;
        //创建位图保存目录
        String path = Environment.getExternalStorageDirectory() + "/test/";
        File sd = new File(path);
        if (!sd.exists()){
            sd.mkdir();
        }

        File file = new File(path+name+".jpg");
        FileOutputStream fileOutputStream = null;
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    fileOutputStream = new FileOutputStream(file);
                    mybitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    //update gallery
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                    //Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                    result = true;

                }
                else{
                    Toast.makeText(context, "不能读取到SD卡", Toast.LENGTH_SHORT).show();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return result;
    }

    private String getTime(long date_temp){
        //时间格式,HH是24小时制，hh是AM PM12小时制
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //比如timestamp=1449210225945；
        String date_string = sdf.format(new Date(date_temp));
        //至于取10位或取13位，date_temp*1000L就是这种截取作用。如果是和服务器传值的，就和后台商量好就可以了
        Log.d("test", date_string);
        return date_string;
    }

    public static Bitmap scrollViewScreenShot(TextView textView) {
        int h = 0;
        Bitmap bitmap = null;
        textView.setBackgroundColor(Color.parseColor("#902F2E2E"));
        h = textView.getHeight();
        bitmap = Bitmap.createBitmap(textView.getWidth(), h, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        textView.draw(canvas);
        return bitmap;
    }

    public boolean createWaterPrint(){
        Bitmap bitmap = scrollViewScreenShot(tv_tip_bottom);
        return saveBitmapToSdCard(activity, bitmap, "test");
    }

    private void initialize(Context context) {
        final View videoCapture = View.inflate(context, R.layout.view_videocapture, this);

        videocapture_cancel = (TextView) videoCapture.findViewById(R.id.videocapture_cancel);
        mRecordBtnIv = (ImageView) videoCapture.findViewById(R.id.videocapture_recordbtn_iv);
        mAcceptBtnIv = (ImageView) videoCapture.findViewById(R.id.videocapture_acceptbtn_iv);
        mDeclineBtnIv = (ImageView) videoCapture.findViewById(R.id.videocapture_declinebtn_iv);
        mChangeCameraIv = (ImageView) videoCapture.findViewById(R.id.change_camera_iv);
        tv_tip_bottom = (TextView)videoCapture.findViewById(R.id.videocapture_tip_bottom);
        tv_tip_bottom.setText("中国音乐家协会考级视频拍摄时间:"+getTime(System.currentTimeMillis()));
        tv_tip_top = (TextView) videoCapture.findViewById(R.id.videocapture_tip_top);
        iv_person = (ImageView) videoCapture.findViewById(R.id.videocapture_person_iv);


        videocapture_cancel.setText("返回");
        videocapture_cancel.setOnClickListener(this);
        mRecordBtnIv.setOnClickListener(this);
        mAcceptBtnIv.setOnClickListener(this);
        mDeclineBtnIv.setOnClickListener(this);
        mChangeCameraIv.setOnClickListener(this);

        mThumbnailIv = (ImageView) videoCapture.findViewById(R.id.videocapture_preview_iv);
        mSurfaceView = (SurfaceView) videoCapture.findViewById(R.id.videocapture_preview_sv);

        mTimerTv = (TextView) videoCapture.findViewById(R.id.videocapture_timer_tv);
        mTimerTv2 = (TextView) videoCapture.findViewById(R.id.videocapture_timer_tv2);
    }

    public void setRecordingButtonInterface(RecordingButtonInterface mBtnInterface) {
        this.mRecordingInterface = mBtnInterface;
    }

    public void setCameraSwitchingEnabled(boolean isCameraSwitchingEnabled) {
        this.isCameraSwitchingEnabled = isCameraSwitchingEnabled;
        mChangeCameraIv.setVisibility(isCameraSwitchingEnabled ? View.VISIBLE : View.INVISIBLE);
    }

    public void setCameraFacing(boolean isFrontFacing) {
        if (!isCameraSwitchingEnabled) return;
        isFrontCameraEnabled = isFrontFacing;
        mChangeCameraIv.setImageResource(isFrontCameraEnabled ?
                R.drawable.ic_change_camera_back :
                R.drawable.ic_change_camera_front);
    }

    public SurfaceHolder getPreviewSurfaceHolder() {
        return mSurfaceView.getHolder();
    }

    public void updateUINotRecording() {
        mRecordBtnIv.setSelected(false);
        mChangeCameraIv.setVisibility(allowCameraSwitching() ? VISIBLE : INVISIBLE);
        mRecordBtnIv.setVisibility(View.VISIBLE);
        mAcceptBtnIv.setVisibility(View.GONE);
        mDeclineBtnIv.setVisibility(View.GONE);
        mThumbnailIv.setVisibility(View.GONE);
        mSurfaceView.setVisibility(View.VISIBLE);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int seconds = (int) (timeInMilliseconds / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            updateRecordingTime(seconds, minutes);
            customHandler.postDelayed(this, 1000);
        }
    };

    public void updateUIRecordingOngoing() {
        mRecordBtnIv.setSelected(true);
        mRecordBtnIv.setVisibility(View.VISIBLE);
        mChangeCameraIv.setVisibility(View.INVISIBLE);
        mAcceptBtnIv.setVisibility(View.GONE);
        mDeclineBtnIv.setVisibility(View.GONE);
        mThumbnailIv.setVisibility(View.GONE);
        mSurfaceView.setVisibility(View.VISIBLE);
        if (mShowTimer) {
            if (showTimer2){
                mTimerTv2.setVisibility(View.VISIBLE);
            }else {
                mTimerTv.setVisibility(View.VISIBLE);
            }
            startTime = SystemClock.uptimeMillis();
            updateRecordingTime(0, 0);
            customHandler.postDelayed(updateTimerThread, 1000);
        }
    }


    public void hideTip(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public void stopTimer(){
        if (timer != null){
            timer.cancel();
        }
        if (timerTask != null){
            timerTask.cancel();
        }
    }

    private Timer timer;
    private TimerTask timerTask;
    private int num = 11;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                num --;
                if (num == 0){
                    tv_tip_bottom.setVisibility(GONE);
                    tv_tip_top.setVisibility(GONE);
                    iv_person.setVisibility(GONE);
                    if (timer != null){
                        timer.cancel();
                    }
                    if (timerTask != null){
                        timerTask.cancel();
                    }
                }else {
                    tv_tip_top.setText("请先完整报幕，然后演唱（演奏）,报幕时人脸需填满虚线框");
                }
            }
        }
    };



    public void updateUIRecordingFinished(Bitmap videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
        mRecordBtnIv.setVisibility(View.INVISIBLE);
        mAcceptBtnIv.setVisibility(View.VISIBLE);
        mChangeCameraIv.setVisibility(View.INVISIBLE);
        mDeclineBtnIv.setVisibility(View.VISIBLE);
        mThumbnailIv.setVisibility(View.VISIBLE);
        mSurfaceView.setVisibility(View.GONE);

        if (videoThumbnail != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(orientationDegree);
            videoThumbnail = Bitmap.createBitmap(videoThumbnail, 0, 0, videoThumbnail.getWidth(), videoThumbnail.getHeight(), matrix, true);
            mThumbnailIv.setScaleType(ScaleType.FIT_CENTER);
            mThumbnailIv.setImageBitmap(videoThumbnail);
        }
        customHandler.removeCallbacks(updateTimerThread);

    }

    @Override
    public void onClick(View v) {
        if (mRecordingInterface == null) return;

        if (v.getId() == mRecordBtnIv.getId()) {
            mRecordingInterface.onRecordButtonClicked();
        } else if (v.getId() == mAcceptBtnIv.getId()) {
            mRecordingInterface.onAcceptButtonClicked();
        } else if (v.getId() == mDeclineBtnIv.getId()) {
            mRecordingInterface.onDeclineButtonClicked();
        } else if (v.getId() == mChangeCameraIv.getId()) {
            isFrontCameraEnabled = !isFrontCameraEnabled;
            mChangeCameraIv.setImageResource(isFrontCameraEnabled ?
                    R.drawable.ic_change_camera_front : R.drawable.ic_change_camera_back);
            mRecordingInterface.onSwitchCamera(isFrontCameraEnabled);
        } else if (v.getId() == R.id.videocapture_cancel) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("退出视频拍摄?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            stopTimer();
                            dialog.dismiss();
                            mRecordingInterface.onDeclineButtonClicked();
//                            activity.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }

    }

    public void showTimer(boolean showTimer) {
        this.mShowTimer = showTimer;
    }

    private void updateRecordingTime(int seconds, int minutes) {
        mTimerTv.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
        mTimerTv2.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
    }

    private boolean allowCameraSwitching() {
        return CapturePreview.isFrontCameraAvailable() && isCameraSwitchingEnabled;
    }


}
