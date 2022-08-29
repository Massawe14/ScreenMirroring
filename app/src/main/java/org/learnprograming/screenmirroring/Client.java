package org.learnprograming.screenmirroring;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Phaneendra on 04-Jan-17.
 */

public class Client extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = "";
    ImageView img;
    Button connect;
    MainClint activity;

    Client(String addr, int port, ImageView i, Button b, MainClint act) {
        dstAddress = addr;
        dstPort = port;

        this.img=i;
        this.connect=b;
        this.activity=act;
    }



    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(activity.buttonConnect.getVisibility()== View.INVISIBLE) {
                    activity.buttonConnect.performClick();
                }
            }
        });

        try {
            socket = new Socket(dstAddress, dstPort);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000*1024);
            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

			/*
			 * notice: inputStream.read() will block if no data return
			 */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                //response += byteArrayOutputStream.toString("UTF-8");
            }
            response=byteArrayOutputStream.toString();
            //System.out.println(response);
            //Log.d("response",response);
            byteArrayOutputStream.close();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //textResponse.setText(response);
        String test="";
        Log.e("test",response);
        img.setImageBitmap(decodeBase64(response));
        super.onPostExecute(result);
    }

    public Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}

