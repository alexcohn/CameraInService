package com.example.camerainservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.IOException;

public class CameraService extends Service {
    private Camera camera;

    public CameraService() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i("SERVICE", "onStart()");
        startPreview();
    }

    void startPreview() {
        camera = Camera.open(0);
        SurfaceView mview = new SurfaceView(this);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        mview.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(holder);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        wm.addView(mview, params);
        mview.setZOrderOnTop(true);
    }

    @Override
    public void onCreate() {
        Log.i("SERVICE", "onCreate()");
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
