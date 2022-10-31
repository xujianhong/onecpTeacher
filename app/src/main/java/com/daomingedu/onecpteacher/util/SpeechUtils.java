package com.daomingedu.onecpteacher.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.util.Locale;

/**
 * Description
 * Created by jianhongxu on 2021/10/22
 */
public class SpeechUtils {
    private Context context;

    private SpeechSynthesizer mTts;
    //    private static final String TAG = "SpeechUtils";
    private static SpeechUtils singleton;
//
//    private TextToSpeech textToSpeech; // TTS对象

    public static SpeechUtils getInstance(Context context){
        return getInstance(context,null);
    }
    public static SpeechUtils getInstance(Context context, InitListener initListener) {
        if (singleton == null) {
            synchronized (SpeechUtils.class) {
                if (singleton == null) {
                    singleton = new SpeechUtils(context, initListener);
                }
            }
        }
        return singleton;
    }

    public SpeechUtils(Context context, InitListener initListener) {
        this.context = context;
        mTts = SpeechSynthesizer.createSynthesizer(context, initListener);
//        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int i) {
//                if (i == TextToSpeech.SUCCESS) {
//                    textToSpeech.setLanguage(Locale.CHINA);
//                    textToSpeech.setPitch(1.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
//                    textToSpeech.setSpeechRate(1.0f);
//                }
//            }
//        });
//

    }

    public void speakText(String text) {
        speakText(text,null);
    }


    public void speakText(String text, SynthesizerListener synthesizerListener) {
        if (mTts != null) {
            mTts.startSpeaking(text, synthesizerListener);
        }

    }

    public void destroy() {
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }
}
