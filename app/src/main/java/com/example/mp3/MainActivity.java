package com.example.mp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.palette.graphics.Palette;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout mainActivity_layout;
    ViewPager2 music_viewPager;
    TextView title_tv,currentTime_tv,durationTime_tv;
    SeekBar mediaPos_sb;
    ImageButton backwardMusic_btn,playToggle_btn,forwardMusic_btn;
    MusicViewPagerAdapter musicViewPagerAdapter;
    ProgressBar musicListPosition_pb;
    MediaPlayer mediaPlayer;
    boolean isPlaying = false, pauseSeekbarUpdate = false;
    int mediaPosition = 0, mediaDuration = 0, mediaCurrentPosition = 0, currentMusicListPosition = 0;
    UiUpdateThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allFindViewById();
    }
    private void allFindViewById(){
        mainActivity_layout = findViewById(R.id.mainActivity_layout);
        music_viewPager =findViewById(R.id.music_viewPager);
        title_tv = findViewById(R.id.title_tv);
        currentTime_tv = findViewById(R.id.currentTime_tv);
        durationTime_tv = findViewById(R.id.durationTime_tv);
        mediaPos_sb = findViewById(R.id.mediaPos_sb);
        backwardMusic_btn = findViewById(R.id.backwardMusic_btn);
        playToggle_btn = findViewById(R.id.playToggle_btn);
        forwardMusic_btn = findViewById(R.id.forward_btn);
        musicListPosition_pb = findViewById(R.id.musicListPosition_pb);
    }
    @Override
    protected void onStart() {
        super.onStart();
        createMusicViewPager();
        backwardMusic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backwardMusic();
            }
        });
        playToggle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying){
                    pauseMusic();
                }else {
                    resumeMusic();
                }
            }
        });
        forwardMusic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forwardMusic();
            }
        });
    }
    private void createMusicViewPager(){
        musicViewPagerAdapter = new MusicViewPagerAdapter(this);
        if (musicViewPagerAdapter.items.size() == 0){
            musicViewPagerAdapter.items.add(new MusicViewPagerItemFragment("thunderclouds", R.raw.thunderclouds, new MusicViewPagerItemFragment.OnContactListener() {
                @Override
                public void onContact(MusicViewPagerItemFragment fragment) {
                    title_tv.setText(fragment.title);
                    if (isPlaying){
                        pauseMusic();
                    }
                    createMediaPlayer(fragment.musicRes);
                    resumeMusic();
                    mediaDuration = mediaPlayer.getDuration();
                    setSeekBarValue();
                    uiUpdate();
                }
                @Override
                public void onChangeColor(Palette.Swatch dominantSwatch) {
                    changeUiColor(dominantSwatch);
                }
            }));
            musicViewPagerAdapter.items.add(new MusicViewPagerItemFragment("a_whiter_shade_of_pale", R.raw.a_whiter_shade_of_pale, new MusicViewPagerItemFragment.OnContactListener() {
                @Override
                public void onContact(MusicViewPagerItemFragment fragment) {
                    title_tv.setText(fragment.title);
                    if (isPlaying){
                        pauseMusic();
                    }
                    createMediaPlayer(fragment.musicRes);
                    resumeMusic();
                    mediaDuration = mediaPlayer.getDuration();
                    setSeekBarValue();
                    uiUpdate();
                }
                @Override
                public void onChangeColor(Palette.Swatch dominantSwatch) {
                    changeUiColor(dominantSwatch);
                }
            }));           musicViewPagerAdapter.items.add(new MusicViewPagerItemFragment("soldier_of_fortune", R.raw.soldier_of_fortune, new MusicViewPagerItemFragment.OnContactListener() {
                @Override
                public void onContact(MusicViewPagerItemFragment fragment) {
                    title_tv.setText(fragment.title);
                    if (isPlaying){
                        pauseMusic();
                    }
                    createMediaPlayer(fragment.musicRes);
                    resumeMusic();
                    mediaDuration = mediaPlayer.getDuration();
                    setSeekBarValue();
                    uiUpdate();
                }
                @Override
                public void onChangeColor(Palette.Swatch dominantSwatch) {
                    changeUiColor(dominantSwatch);
                }
            }));           musicViewPagerAdapter.items.add(new MusicViewPagerItemFragment("thunderclouds", R.raw.thunderclouds, new MusicViewPagerItemFragment.OnContactListener() {
                @Override
                public void onContact(MusicViewPagerItemFragment fragment) {
                    title_tv.setText(fragment.title);
                    if (isPlaying){
                        pauseMusic();
                    }
                    createMediaPlayer(fragment.musicRes);
                    resumeMusic();
                    mediaDuration = mediaPlayer.getDuration();
                    setSeekBarValue();
                    uiUpdate();
                }
                @Override
                public void onChangeColor(Palette.Swatch dominantSwatch) {
                    changeUiColor(dominantSwatch);
                }
            }));
            musicViewPagerAdapter.items.add(new MusicViewPagerItemFragment("under_pressure", R.raw.under_pressure, new MusicViewPagerItemFragment.OnContactListener() {
                @Override
                public void onContact(MusicViewPagerItemFragment fragment) {
                    title_tv.setText(fragment.title);
                    if (isPlaying){
                        pauseMusic();
                    }
                    createMediaPlayer(fragment.musicRes);
                    resumeMusic();
                    mediaDuration = mediaPlayer.getDuration();
                    setSeekBarValue();
                    uiUpdate();
                }
                @Override
                public void onChangeColor(Palette.Swatch dominantSwatch) {
                    changeUiColor(dominantSwatch);
                }
            }));
        }
        music_viewPager.setAdapter(musicViewPagerAdapter);
        musicListPosition_pb.setMax((musicViewPagerAdapter.getItemCount()-1)*1000);
        music_viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                int progress =(int) ((position + positionOffset)*1000);
                musicListPosition_pb.setProgress(progress);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentMusicListPosition = position;
            }
        });
    }
    private void createMediaPlayer(int res){
        mediaPlayer = MediaPlayer.create(getApplicationContext(), res);
        mediaPlayer.seekTo(0);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (currentMusicListPosition < musicViewPagerAdapter.getItemCount()-1){
                    music_viewPager.setCurrentItem(currentMusicListPosition + 1);
                }else {
                    music_viewPager.setCurrentItem(0);
                }
            }
        });
    }
    private void pauseMusic(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPosition = mediaPlayer.getCurrentPosition();
            isPlaying = false;
            upDateButtonImage();
        }
    }
    private void resumeMusic(){
        mediaPlayer.seekTo(mediaPosition);
        mediaPlayer.start();
        isPlaying = true;
        upDateButtonImage();
    }
    private void backwardMusic(){
        if (mediaCurrentPosition > 1000){
            pauseMusic();
            mediaPosition = 0;
            resumeMusic();
        }else {
            if (currentMusicListPosition > 0){
                music_viewPager.setCurrentItem(musicViewPagerAdapter.getItemCount()-1);
            }
        }
    }
    private void forwardMusic(){
        if (mediaCurrentPosition < mediaDuration - 1000){
            pauseMusic();
            mediaPosition = mediaDuration - 1000;
            resumeMusic();
        }else {
            if (currentMusicListPosition < musicViewPagerAdapter.getItemCount()-1){
                music_viewPager.setCurrentItem(currentMusicListPosition + 1);
            }else {
                music_viewPager.setCurrentItem(0);
            }
        }
    }
    private void  upDateButtonImage(){
        if (isPlaying){
            playToggle_btn.setBackgroundResource(R.drawable.ic_action_pause);
        }else {
            playToggle_btn.setBackgroundResource(R.drawable.ic_action_play);
        }
    }
    private void setSeekBarValue(){
        mediaPos_sb.setMax(mediaDuration);
        mediaPos_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (thread != null){
                    pauseSeekbarUpdate = true;
                }
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (thread != null){
                    pauseSeekbarUpdate = false;
                }
                mediaPlayer.seekTo(mediaPos_sb.getProgress());
            }
        });
    }
    private void uiUpdate(){
        if (thread != null){
            thread.interrupt();
        }
        thread = new UiUpdateThread();
        thread.start();
    }
    private void changeUiColor(Palette.Swatch paletteSwatch){
        mainActivity_layout.setBackgroundColor(paletteSwatch.getRgb());
        mediaPos_sb.setProgressTintList(ColorStateList.valueOf(paletteSwatch.getRgb()));
        mediaPos_sb.setThumbTintList(ColorStateList.valueOf(paletteSwatch.getRgb()));
        musicListPosition_pb.setProgressTintList(ColorStateList.valueOf(paletteSwatch.getTitleTextColor()));
        title_tv.setTextColor(paletteSwatch.getTitleTextColor());
    }

    class MusicViewPagerAdapter extends FragmentStateAdapter {
        ArrayList<Fragment> items = new ArrayList<>();

        public MusicViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return items.get(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
    class UiUpdateThread extends Thread{
        UiUpdateHandler handler = new UiUpdateHandler();

        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    if (mediaPlayer != null){
                        mediaCurrentPosition = mediaPlayer.getCurrentPosition();
                        mediaDuration = mediaPlayer.getDuration();
                        String currentTimeLabel = createTimeLabel(mediaCurrentPosition);
                        String durationTimeLabel = createTimeLabel(mediaDuration);
                        Bundle bundle = new Bundle();
                        bundle.putInt("mediaCurrentPosition", mediaCurrentPosition);
                        bundle.putInt("mediaDuration", mediaDuration);
                        bundle.putString("currentTimeLabel", currentTimeLabel);
                        bundle.putString("durationTimeLabel", durationTimeLabel);
                        Message message = handler.obtainMessage();
                        message.setData(bundle);
                        handler.sendMessage(message);
                        bundle = null;
                        try {
                           Thread.sleep(1);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                            break;
                        }
                    }
                } catch (Exception e){

                }
            }
        }
        private String createTimeLabel(int msec){
            String timeLabel;

            int min = msec / 1000 / 60;
            int sec = msec / 1000 % 60;
            timeLabel = min + ":";
            if (sec < 10){
                timeLabel += "0";
            }
            timeLabel += sec;
            return timeLabel;
        }

    }
    class UiUpdateHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            currentTime_tv.setText(msg.getData().getString("currentTimeLabel"));
            durationTime_tv.setText(msg.getData().getString("durationTimeLabel"));
            if (mediaPos_sb.getMax() != mediaDuration){
                mediaPos_sb.setMax(mediaDuration);
            }
            if (!pauseSeekbarUpdate){
                mediaPos_sb.setProgress(msg.getData().getInt("mediaCurrentPosition"));
            }
        }

    }
}