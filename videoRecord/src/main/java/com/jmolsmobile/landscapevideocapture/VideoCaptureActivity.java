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

package com.jmolsmobile.landscapevideocapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jmolsmobile.landscapevideocapture.camera.CameraWrapper;
import com.jmolsmobile.landscapevideocapture.camera.NativeCamera;
import com.jmolsmobile.landscapevideocapture.configuration.CaptureConfiguration;
import com.jmolsmobile.landscapevideocapture.recorder.AlreadyUsedException;
import com.jmolsmobile.landscapevideocapture.recorder.VideoRecorder;
import com.jmolsmobile.landscapevideocapture.recorder.VideoRecorderInterface;
import com.jmolsmobile.landscapevideocapture.view.RecordingButtonInterface;
import com.jmolsmobile.landscapevideocapture.view.VideoCaptureView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import VideoHandle.EpDraw;
import VideoHandle.EpEditor;
import VideoHandle.EpText;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import androidx.annotation.NonNull;

public class VideoCaptureActivity extends Activity implements RecordingButtonInterface, VideoRecorderInterface {

    public static final int RESULT_ERROR = 753245;
    private static final int REQUESTCODE_SWITCHCAMERA = 578465;

    public static final String EXTRA_OUTPUT_FILENAME = "com.jmolsmobile.extraoutputfilename";
    public static final String EXTRA_CAPTURE_CONFIGURATION = "com.jmolsmobile.extracaptureconfiguration";
    public static final String EXTRA_ERROR_MESSAGE = "com.jmolsmobile.extraerrormessage";
    public static final String WATER = "water";

    private static final String EXTRA_FRONTFACINGCAMERASELECTED = "com.jmolsmobile.extracamerafacing";
    private static final String SAVED_RECORDED_BOOLEAN = "com.jmolsmobile.savedrecordedboolean";
    protected static final String SAVED_OUTPUT_FILENAME = "com.jmolsmobile.savedoutputfilename";

    private int videoType = 0;

    private boolean mVideoRecorded = false;
    VideoFile mVideoFile = null;
    private CaptureConfiguration mCaptureConfiguration;

    private VideoCaptureView mVideoCaptureView;
    private VideoRecorder mVideoRecorder;
    private boolean isFrontFacingCameraSelected;
    private CameraWrapper mCameraWrapper;
//    private MyOrientationDetector orientationDetector;

    private int width = 10;
    private int height = 10;

    private int water = 1;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CLog.toggleLogging(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_videocapture);

        initializeCaptureConfiguration(savedInstanceState);

        mVideoCaptureView = (VideoCaptureView) findViewById(R.id.videocapture_videocaptureview_vcv);
        if (mVideoCaptureView == null) return; // Wrong orientation

        initializeRecordingUI();
//        orientationDetector = new MyOrientationDetector(this, new MyOrientationDetector.OnOrientationChangeListener() {
//            @Override
//            public void changed(int degree) {
//                mVideoRecorder.setOrientationHint(degree);
//                mVideoCaptureView.rotateViews(-degree);
//            }
//        });
        int degree = 0;
        mVideoRecorder.setOrientationHint(mCameraWrapper.getmNativeCamera().cameraOrientation);
        mVideoCaptureView.rotateViews(degree);
    }





    private void initializeCaptureConfiguration(final Bundle savedInstanceState) {
        mCaptureConfiguration = generateCaptureConfiguration();
        mVideoRecorded = generateVideoRecorded(savedInstanceState);
        isFrontFacingCameraSelected = generateIsFrontFacingCameraSelected();

        videoType = getIntent().getIntExtra("videoType", 0);
        water = getIntent().getIntExtra("water", 1);
        Log.d("test", ""+water);
        mVideoFile = generateOutputFile(savedInstanceState);
    }



    private void initializeRecordingUI() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        mCameraWrapper =  new CameraWrapper(new NativeCamera(), display.getRotation());
        mVideoRecorder = new VideoRecorder(this,
                mCaptureConfiguration,
                mVideoFile,
                mCameraWrapper,
                mVideoCaptureView.getPreviewSurfaceHolder(),
                isFrontFacingCameraSelected, mVideoCaptureView);
        mVideoCaptureView.setRecordingButtonInterface(this);
        mVideoCaptureView.setCameraSwitchingEnabled(mCaptureConfiguration.getAllowFrontFacingCamera());
        mVideoCaptureView.setCameraFacing(isFrontFacingCameraSelected);

        if (mVideoRecorded) {
            mVideoCaptureView.updateUIRecordingFinished(getVideoThumbnail());
        } else {
            mVideoCaptureView.updateUINotRecording();
        }
        mVideoCaptureView.showTimer(mCaptureConfiguration.getShowTimer());
    }

    @Override
    protected void onPause() {
        if (mVideoRecorder != null) {
            mVideoRecorder.stopRecording(null);
        }
        releaseAllResources();
//        orientationDetector.disable();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finishCancelled();
    }

    @Override
    public void onRecordButtonClicked() {
        try {
            mVideoRecorder.toggleRecording();
        } catch (AlreadyUsedException e) {
            CLog.d(CLog.ACTIVITY, "Cannot toggle recording after cleaning up all resources");
        }
    }

    @Override
    public void onAcceptButtonClicked() {
        finishCompleted();
    }

    @Override
    public void onDeclineButtonClicked() {
        finishCancelled();
    }

    @Override
    public void onRecordingStarted() {
        mVideoCaptureView.hideTip();
        mVideoCaptureView.updateUIRecordingOngoing();
//        orientationDetector.disable();
    }

    @Override
    public void onSwitchCamera(boolean isFrontFacingSelected) {
        Intent intent = new Intent(VideoCaptureActivity.this, VideoCaptureActivity.class);
        intent.putExtras(getIntent().getExtras());      //Pass all the current intent parameters
        intent.putExtra(EXTRA_FRONTFACINGCAMERASELECTED, isFrontFacingSelected);

        startActivityForResult(intent, REQUESTCODE_SWITCHCAMERA);
        overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
    }

    @Override
    public void onRecordingStopped(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        mVideoCaptureView.updateUIRecordingFinished(getVideoThumbnail());
        releaseAllResources();
//        orientationDetector.enable();
    }

    private String getTime(long date_temp){
        //时间格式,HH是24小时制，hh是AM PM12小时制
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH_mm");
        //比如timestamp=1449210225945；
        String date_string = sdf.format(new Date(date_temp));
        //至于取10位或取13位，date_temp*1000L就是这种截取作用。如果是和服务器传值的，就和后台商量好就可以了
        Log.d("test", date_string);
        return date_string;
    }

    @Override
    public void onRecordingSuccess() {
        mVideoRecorded = true;
    }

    @Override
    public void onRecordingFailed(String message) {
        finishError(message);
    }

    private void finishCompleted() {
            if (water == 1){
                progressDialog = new ProgressDialog(VideoCaptureActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("视频生成中");
                progressDialog.show();
            }
            MediaScannerConnection.scanFile(this,
                    new String[]{mVideoFile.getFullPath()},
                    new String[]{"video/mp4"},
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(final String path, Uri uri) {
                            Log.i("TAG","onScanCompleted"+path);
                            if (videoType == 0){
                                outFile = "/storage/emulated/0/DCIM/Camera/"+getTime(System.currentTimeMillis())+".mp4";
                            }else if (videoType == 1){
                                outFile =  getExternalFilesDir("Movies") + "/" + getTime(System.currentTimeMillis())+".mp4";
                            }

                            if (water == 1){

                                width = mCameraWrapper.getRecord_width();
                                height = mCameraWrapper.getRecord_height();
                                EpVideo epVideo = new EpVideo(path);

                                String img_path = Environment.getExternalStorageDirectory() + "/test/test.png";
                                Bitmap bitmap = BitmapFactory.decodeFile(img_path);

                                EpDraw epDraw = new EpDraw(img_path, (int) ((width-(width/1.2))/2), (int) (height-(bitmap.getHeight()*1.5)), (float) (width/1.2),bitmap.getHeight(),false);
                                epVideo.addDraw(epDraw);

                                EpEditor.OutputOption outputOption = new EpEditor.OutputOption(outFile);
                                EpEditor.exec(epVideo, outputOption, new OnEditorListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("test", "onSuccess");
                                        File file = new File(path);
                                        if (file.exists()){
                                            file.delete();
                                        }
                                        handler.sendEmptyMessage(0);
                                    }
                                    @Override
                                    public void onFailure() {
                                        Log.d("test", "onFailure");
                                        handler.sendEmptyMessage(1);
                                    }
                                    @Override
                                    public void onProgress(float progress) {
                                        Log.d("test", progress+"");
                                        progressDialog.setMax(100);
                                        progressDialog.setProgress((int) (progress * 100));
                                    }
                                });
                            }else {
                                outFile = path;
                                handler.sendEmptyMessage(0);
                            }
                        }
                    });
    }


    AlertDialog alertDialog = null;
    String outFile = "";

    ProgressDialog progressDialog = null;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progressDialog != null){
                progressDialog.dismiss();
            }
            if (msg.what == 0){
                final Intent result = new Intent();
                result.putExtra(EXTRA_OUTPUT_FILENAME, outFile);
                setResult(RESULT_OK, result);
                finish();
            }else if (msg.what == 1){
                Toast.makeText(VideoCaptureActivity.this, "视频添加水印失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void finishCancelled() {
        this.setResult(RESULT_CANCELED);
        if(mVideoFile.getFile().exists()){
            mVideoFile.getFile().delete();
        }
        finish();
    }

    private void finishError(final String message) {
        Toast.makeText(getApplicationContext(), "Can't capture video: " + message, Toast.LENGTH_LONG).show();

        final Intent result = new Intent();
        result.putExtra(EXTRA_ERROR_MESSAGE, message);
        this.setResult(RESULT_ERROR, result);
        finish();
    }

    private void releaseAllResources() {
        if (mVideoRecorder != null) {
            mVideoRecorder.releaseAllResources();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(SAVED_RECORDED_BOOLEAN, mVideoRecorded);
        savedInstanceState.putString(SAVED_OUTPUT_FILENAME, mVideoFile.getFullPath());
        super.onSaveInstanceState(savedInstanceState);
    }

    protected CaptureConfiguration generateCaptureConfiguration() {
        CaptureConfiguration returnConfiguration = this.getIntent().getParcelableExtra(EXTRA_CAPTURE_CONFIGURATION);
        Log.d("test", "time="+returnConfiguration.getMaxCaptureDuration());
        if (returnConfiguration == null) {
            returnConfiguration = CaptureConfiguration.getDefault();
            CLog.d(CLog.ACTIVITY, "No captureconfiguration passed - using default configuration");
        }
        return returnConfiguration;
    }

    private boolean generateVideoRecorded(final Bundle savedInstanceState) {
        if (savedInstanceState == null) return false;
        return savedInstanceState.getBoolean(SAVED_RECORDED_BOOLEAN, false);
    }

    protected VideoFile generateOutputFile(Bundle savedInstanceState) {
        VideoFile returnFile;
        if (savedInstanceState != null) {
            returnFile = new VideoFile(savedInstanceState.getString(SAVED_OUTPUT_FILENAME), water, this);
        } else {
            returnFile = new VideoFile(this.getIntent().getStringExtra(EXTRA_OUTPUT_FILENAME), water, this);
        }
        return returnFile;
    }

    private boolean generateIsFrontFacingCameraSelected() {
        return getIntent().getBooleanExtra(EXTRA_FRONTFACINGCAMERASELECTED, false);
    }

    public Bitmap getVideoThumbnail() {
        final Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(mVideoFile.getFullPath(),
                Thumbnails.FULL_SCREEN_KIND);
        if (thumbnail == null) {
            CLog.d(CLog.ACTIVITY, "Failed to generate video preview");
        }
        return thumbnail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.setResult(resultCode, data);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        orientationDetector.enable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoCaptureView.stopTimer();
    }
}
