package com.example.administrator.musicbox;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;

/*
 * musicService用于管理mediaplayer的播放
 */

public class MusicService extends Service{

    public MediaPlayer mediaPlayer = null;
    public final Binder binder = new MyBinder();   //通过 Binder 来保持 Activity 和 Service 的通信
    public class MyBinder extends Binder{          //定义一个Mybinder类，继承Binder
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public Binder onBind(Intent intent){
        return binder;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){ //如果还有service任务，就停止并释放
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public MusicService(){
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource( Environment.getExternalStorageDirectory().toString() +"/song/melt.mp3" );
            mediaPlayer.prepare();       //音乐播放就绪
            mediaPlayer.setLooping(true);   //设置循环播放
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void play_button_control() {       // 控制播放/暂停，由main activity设置监听器控制触发
        if (mediaPlayer != null) {            //如果mediaplayer已经加载到了资源
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();          //如果正在播的话就暂停
            } else {
                mediaPlayer.start();          //否则就从当前位置开始播放
            }
        }
    }
    public void stop_button_control() {        //控制停止，由监听器控制触发
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();          //进入就绪
                mediaPlayer.seekTo(0);          //seekTo:设定播放时间指定位置，0：初始位置，单位ms
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
