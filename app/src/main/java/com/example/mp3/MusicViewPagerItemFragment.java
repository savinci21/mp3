package com.example.mp3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

public class MusicViewPagerItemFragment extends Fragment {
    interface OnContactListener {
        void onContact(MusicViewPagerItemFragment fragment);
        void onChangeColor(Palette.Swatch dominantSwatch);
    }

    View rootView;
    ImageView albumArt_iv;
    Bitmap bitmap;
    CrateBitmapThread crateBitmapThread;
    GenerateBitmapColorThread generateBitmapColorThread;
    OnContactListener listener;
    String title;
    int musicRes;
    Palette.Swatch dominantSwatch;

    public MusicViewPagerItemFragment(String title, int musicRes, OnContactListener listener) {
        this.title = title;
        this.musicRes = musicRes;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.musiclist_item_viewpager, container, false);
        albumArt_iv = findViewById(R.id.albumArt_tv);
        crateBitmapThread = new CrateBitmapThread();
        crateBitmapThread.start();
        return rootView;
    }

    protected <T extends View> T findViewById(int id){return rootView.findViewById(id);}

    @Override
    public void onResume() {
        super.onResume();
        listener.onContact(this);
    }

    class CrateBitmapThread extends Thread {
        CreateBitmapHandler handler = new CreateBitmapHandler();
        @Override
        public void run() {
            super.run();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.albumimage);
            Bundle bundle = new Bundle();
            Message message = handler.obtainMessage();
            try {
                Thread.sleep(1);
                message.setData(bundle);
                handler.sendMessage(message);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    class CreateBitmapHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            albumArt_iv.setImageBitmap(bitmap);
            generateBitmapColorThread = new GenerateBitmapColorThread();
            generateBitmapColorThread.start();
        }
    }
    class GenerateBitmapColorThread extends Thread{
        GenerateBitmapColorHandler handler = new GenerateBitmapColorHandler();
        @Override
        public void run() {
            super.run();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    dominantSwatch = palette.getDominantSwatch();
                    Message message = handler.obtainMessage();
                    handler.sendMessage(message);
                }
            });
        }
    }
    class GenerateBitmapColorHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            listener.onChangeColor(dominantSwatch);
        }
    }

}


