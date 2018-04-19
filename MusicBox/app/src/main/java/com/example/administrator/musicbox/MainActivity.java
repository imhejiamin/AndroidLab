package com.example.administrator.musicbox;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView cover; //封面
    private SeekBar seekBar; //进度条

    private Button stop;
    private Button play;
    private Button quit;

    private TextView music_state; //音乐播放进度
    private TextView duration; //音乐的总长度
    private TextView show_text; //文字展示状态

    private MusicService musicService;//实例music service
    private float degree = 0; //封面旋转度数

    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss"); //格式时间 mm:ss

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initial过程,绑定service
        Intent intent = new Intent(this,MusicService.class);
        startService(intent);
        bindService(intent,sc, Context.BIND_AUTO_CREATE); //将service绑定到main activity

        cover = (ImageView)findViewById(R.id.cover);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        play = (Button)findViewById(R.id.play);
        stop = (Button)findViewById(R.id.stop);
        quit = (Button)findViewById(R.id.quit);
        music_state = (TextView)findViewById(R.id.state);
        duration = (TextView)findViewById(R.id.duration);
        show_text = (TextView)findViewById(R.id.show_text);

        //对三个按钮设置监听器，后面重载统一的onclick方法
        play.setOnClickListener(this);
        quit.setOnClickListener(this);
        stop.setOnClickListener(this);
        //对seekbar设置seekbar监听器
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

     //创建并实例化seekbar监听器，管理人为拉动进度条seekbar的更新
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener(){
        @Override
        public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {  //如果进度位置被人为拉动改变了
            music_state.setText(timeFormat.format(s.getProgress()));  //获取seekbar当前进度，格式时间，显示在 music_state
        }
        @Override
        public void onStartTrackingTouch(SeekBar s) { } //开始拖动进度条时调用

        @Override
        public void onStopTrackingTouch(SeekBar s) {  //停止拖动进度条时调用
            musicService.mediaPlayer.seekTo(s.getProgress());   //停止拖动进度条，将当前进度返回给 media player，如果暂停就直接从当前进度播放
        }
    };

    // sc ,bindService()的参数 ----用于绑定service
    private ServiceConnection sc = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name , IBinder iBinder){
            musicService = new MusicService();       // 构造music service
            musicService = ((MusicService.MyBinder)(iBinder)).getService(); //利用binder实例music service 并绑定

            seekBar.setMax(musicService.mediaPlayer.getDuration());           //设置获取当前歌曲长度并设置成seekbar的最大值
            duration.setText(timeFormat.format(musicService.mediaPlayer.getDuration()));  //获取歌曲长度，格式时间并显示在duration
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {     //解绑
            sc = null;
           // musicService = null;
        }
    };
   /* private void initial(){
        Intent intent = new Intent(this,MusicService.class);
        startService(intent);
        bindService(intent,sc, Context.BIND_AUTO_CREATE);
    }*/

    @Override
    public boolean onKeyDown(int keyCode , KeyEvent keyEvent){  //监听手机屏幕按键
        if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK){     //用户点击了返回键KEYCODE_BACK
            moveTaskToBack(true);                               //将任务放到后台运行
        }
        return super.onKeyDown(keyCode, keyEvent);
    }


    @Override
    public void onClick(View view){    //重写onclick控制点击变化
        try{
            switch(view.getId()){       //检测点击了什么按钮，执行对应操作
                case R.id.play: Play();  break;
                case R.id.stop: Stop();  break;
                case R.id.quit: Quit();  break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // Handler与UI是同一线程，可以通过Handler更新UI上的组件状态：需要更新的是seekbar的进度移动和封面旋转
    private Handler handler = new Handler();    //创建一个handler
    Thread UpdateThread = new Thread() {   //创建一个Runnable接口实现的更新线程
        @Override
        public void run() {
            if(musicService.mediaPlayer.isPlaying()) {
                seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());  // 获取音乐当前进度，更新到seekbar
                cover.setRotation(degree += 0.15);           //封面图片每次更新旋转0.15度
                handler.postDelayed(UpdateThread, 10);      //设置线程的更新频率：10ms，利用handler更新到UI
            }
        }
    };
    public void Play(){
        //直接更新按钮对应的textview
        if(musicService.mediaPlayer.isPlaying()){
            play.setText("Play");
            show_text.setText("Paused.");
        }
        else {
            play.setText("Pause");
            show_text.setText("Playing.");
        }

        musicService.play_button_control(); //通过service控制播放器Media player
        handler.post(UpdateThread); //使用handler更新图片和seekbar

    }

    public void Stop(){
        play.setText("play");
        show_text.setText("Stopped.");
        musicService.stop_button_control();  //通过service控制播放器Media player
        seekBar.setProgress(0);
        degree = 0;
        cover.setRotation(degree);
    }

    public void Quit(){
        unbindService(sc);    //解绑service connection
        try{
            MainActivity.this.finish(); //结束mian activity并退出
            System.exit(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
