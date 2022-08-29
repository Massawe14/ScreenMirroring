package org.learnprograming.screenmirroring;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Phaneendra on 04-Jan-17.
 */

public class Server {
    MainServer activity;
    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 8080;
    static int imgcnt=0;
    static final int max_img_cnt=200;

    public Server(MainServer activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(socketServerPORT);

                while (true) {
                    Socket socket = serverSocket.accept();
                    count++;
                    message += "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n";

                    activity.runOnUiThread(() -> activity.msg.setText(message));

                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(
                            socket, count);
                    socketServerReplyThread.run();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private class SocketServerReplyThread extends Thread {

        private final Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;

            String msgReply = encodeToBase64(loadImageFromStorage(Environment.getExternalStorageDirectory()+"/captures/"), Bitmap.CompressFormat.JPEG, 70);

            try {
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();

                //message += "replayed: " + msgReply + "\n";
                message+="--";
                activity.runOnUiThread(() -> activity.msg.setText(message));

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e + "\n";
            }

            activity.runOnUiThread(() -> activity.msg.setText(message));
        }

    }

    public String getIpAddress() {
        StringBuilder ip = new StringBuilder();
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip.append("Server running at : ").append(inetAddress.getHostAddress());
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip.append("Something Wrong! ").append(e).append("\n");
        }
        return ip.toString();
    }

    public String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }



    private Bitmap loadImageFromStorage(String path)
    {
        Bitmap b=null;
        try {

            File f=new File(path, "capturedscreenandroid"+ imgcnt +".jpg");
            imgcnt++;
            if(imgcnt==max_img_cnt)
            {
                imgcnt=0;
            }
            b = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        System.out.println(b);
        return b;
    }


}

