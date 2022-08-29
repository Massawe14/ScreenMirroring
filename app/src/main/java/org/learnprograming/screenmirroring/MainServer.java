package org.learnprograming.screenmirroring;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MainServer extends Activity {

    Server server;
    TextView info_ip, msg;

    private MediaProjectionManager projectionManager;
    private int displayWidth,displayHeight,imagesProduced=0;
    ImageReader imageReader;
    private final android.os.Handler handler;

    public MainServer(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server);

        projectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);


        info_ip = (TextView) findViewById(R.id.info_ip);
        msg = (TextView) findViewById(R.id.msg);
        server = new Server(this);
        info_ip.setText(server.getIpAddress()+":"+server.getPort());
        startActivityForResult(projectionManager.createScreenCaptureIntent(), 999);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.onDestroy();
    }

    @SuppressLint({"NewApi", "WrongConstant"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999) {
            MediaProjection mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection != null) {

                //projectionStarted = true;
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int density = metrics.densityDpi;
                int flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY
                        | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                displayHeight=size.y;
                displayWidth=size.x;


                imageReader = ImageReader.newInstance(size.x, size.y, PixelFormat.RGBA_8888, 2);

                mediaProjection.createVirtualDisplay("screencap",
                        size.x, size.y, density,
                        flags, imageReader.getSurface(), null, handler);
                imageReader.setOnImageAvailableListener(new ImageAvailableListener(), handler);
            }
        }
    }


    @SuppressLint("NewApi")
    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {

            Bitmap bitmap = null;
            ByteArrayOutputStream stream = null;
            try (Image image = imageReader.acquireLatestImage()) {
                if (image != null) {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * displayWidth;

                    // create bitmap
                    bitmap = Bitmap.createBitmap(displayWidth + rowPadding / pixelStride,
                            displayHeight, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);

                    //if (skylinkConnection != null && !TextUtils.isEmpty(currentRemotePeerId)) {
                    stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream);
                    createImage(bitmap, imagesProduced);
                    //skylinkConnection.sendData(currentRemotePeerId, stream.toByteArray());
                    //Log.d(TAG, "sending data to peer :" + currentRemotePeerId);
                    //}

                    imagesProduced++;
                    int max_imageno = 200;
                    if (imagesProduced == max_imageno) {
                        imagesProduced = 0;
                    }
                    Log.e("hi", "captured image: " + imagesProduced);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                if (bitmap != null) {
                    bitmap.recycle();
                }

            }
        }
    }




    public void createImage(Bitmap bmp,int i) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

        File file1 = new File(Environment.getExternalStorageDirectory() +"/captures");
        file1.mkdir();

        File file = new File(Environment.getExternalStorageDirectory() +
                "/captures/capturedscreenandroid"+i+".jpg");
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
            //Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}