package com.example.camerainservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraService extends Service {
    private Camera camera;
    private SurfaceView mview;
    private WindowManager wm;

    public CameraService() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i("SERVICE", "onStart()");
        startPreview();
    }

    void startPreview() {
        camera = Camera.open(0);
        mview = new SurfaceView(this);
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
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
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        Log.i("SERVICE", "JPEG received " + data.length);
                        File file = new File(Environment.getExternalStorageDirectory(), "CameraService");
                        try {
                            file.mkdir();
                        }
                        catch (Exception ex) {
                            Log.e("SERVICE", "mkdir failed", ex);
                        }
                        file = new File(file, "qqq.jpg");
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(data);
                            fileOutputStream.close();
                            Log.i("SERVICE", "write complete");
                        }
                        catch (Exception ex) {
                            Log.e("SERVICE", "write failed");
                        }

                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        file = new File(Environment.getExternalStorageDirectory(), "CameraService");
                        file = new File(file, "qqqq.jpg");
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            fileOutputStream.close();
                            Log.i("SERVICE", "write from bitmap complete");
                        }
                        catch (Exception ex) {
                            Log.e("SERVICE", "write from bitmap failed", ex);
                        }


                        stopPreview();
                    }
                });
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

    private void stopPreview() {
        camera.stopPreview();
        camera.release();
        wm.removeView(mview);
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
