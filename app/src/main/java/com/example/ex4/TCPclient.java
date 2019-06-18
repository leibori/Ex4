package com.example.ex4;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TCPclient extends AppCompatActivity implements Joystick.JoystickListener {
    private   Socket socket;
    public PrintWriter writer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Joystick joystick =new Joystick(this);
        setContentView(joystick);
        String ip = getIntent().getStringExtra("ip");
        String port = getIntent().getStringExtra("port");
        connect(ip,port);
    }

    //public TCPclient(){ }
    public void sendMessage(final double send_elevator, final double  send_aileron) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                String elavatorPath = "set controls/flight/elevator ";
//                String aileronPath = "set controls/flight/aileron ";
//                try {
//                    if (socket!=null) {
//
//                        writer.println(elavatorPath + send_elevator + "\r\n");
//                        writer.println(aileronPath + send_aileron + "\r\n");
//                    }
//                } catch (Exception e) {
//                    Log.e("TCP", "S: Error", e);
//                }
//            }
//        };
//        Thread thread = new Thread(runnable);
//        thread.start();

        String elavatorPath = "set controls/flight/elevator ";
        String aileronPath = "set controls/flight/aileron ";
        try {
            if (socket!=null) {

                writer.println(elavatorPath + send_elevator + "\r\n");
                writer.println(aileronPath + send_aileron + "\r\n");
            }
        } catch (Exception e) {
            Log.e("TCP", "S: Error", e);
        }
    }
    public void connect(final String ip, final String port){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    InetAddress serverAddr = InetAddress.getByName(ip);
                    int portNum =Integer.parseInt(port);

                    socket = new Socket(serverAddr, portNum);
                    OutputStream output = socket.getOutputStream();
                    writer = new PrintWriter(output, true);


                }catch (Exception e){
                    System.out.println(e.toString());
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void close(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    socket.close();
                } catch (Exception e) {
                    Log.e("TCP", "S: Error", e);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    @Override
    public void OnJoystickMoved(float xPrecent, float yPrecent, int id) {
    sendMessage(yPrecent,xPrecent);    }

    @Override
    protected void onDestroy() {
        close();
        super.onDestroy();
    }
}
